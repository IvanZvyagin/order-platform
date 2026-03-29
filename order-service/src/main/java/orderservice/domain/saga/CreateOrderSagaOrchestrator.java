package orderservice.domain.saga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.ErrorCode;
import exception.OrderServiceException;
import http.order.*;
import inventoryservice.grpc.ProductInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.api.dto.OrderCreatedEvent;
import orderservice.domain.entity.OrderEntity;
import orderservice.domain.entity.OrderItemEntity;
import orderservice.domain.entity.UserEntity;
import orderservice.domain.grpc.InventoryGrpcClient;
import orderservice.domain.service.orders.OrderStatusPersistenceService;
import orderservice.domain.utils.OrderItemMapper;
import orderservice.domain.utils.UserJpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderSagaOrchestrator {
    private final InventoryGrpcClient inventoryClient;
    private final UserJpaRepository userRepository;
    private final SagaLogJpaRepository sagaRepository;
    private final ObjectMapper objectMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderStatusPersistenceService orderPersistenceService;

    /**
     * Запускает Сагу создания заказа.
     *
     * @param request DTO с данными заказа
     * @return созданный заказ в виде DTO
     */
    @Transactional
    public OrderDto createOrderSaga(OrderCreateRequestDto request) {
        UUID sagaId = UUID.randomUUID();
        saveSagaLog(sagaId, "STARTED", request);
        UserEntity user = getCurrentUser();

        try {

            List<ProductInfo> productInfos = checkItemsAvailability(request.items());
            updateSagaState(sagaId, "ITEM_RESERVED");

            reserveItems(request.items(), sagaId);
            updateSagaState(sagaId, "ITEMS_RESERVED");

            OrderEntity order = buildOrder(user, request.items(), productInfos);
            OrderCreatedEvent event = buildOrderCreatedEvent(order, productInfos);
            OrderEntity saveOrder = orderPersistenceService.saveOrderWithOutbox(order, event);
            updateSagaState(sagaId, "ORDER_CREATED");

            updateSagaState(sagaId, "COMPLETED");
            log.info("Saga {} completed successfully ", sagaId);

            return buildOrderDto(saveOrder, productInfos);
        }catch (OrderServiceException e){
            log.warn("Saga {} failed woth buissnes error: {}", sagaId, e.getErrorCode());
            compensate(sagaId, request.items());
            updateSagaState(sagaId, "FAILED");
            throw e;
        } catch (Exception e) {
            log.error("Saga {} failed, starting compensation", sagaId);
            compensate(sagaId, request.items());
            updateSagaState(sagaId, "FAILED");
            throw new OrderServiceException(ErrorCode.ORDER_CREATION_FAILED);
        }
    }

    /**
     * Проверка доступности товара на складе
     */
    private List<ProductInfo> checkItemsAvailability(List<OrderItemRequestDto> items) {
        List<ProductInfo> infos = new ArrayList<>();
        for (OrderItemRequestDto item : items) {
            try {
                ProductInfo info = inventoryClient.checkAndGet(item.productId(), item.quantity());
                infos.add(info);
            } catch (Exception e) {
                String message = e.getMessage() != null ? e.getMessage() : "Недостаточно товара на складе. Запрошено: "
                        + item.quantity();
                log.error("Failed to check product {}: {}", item.productId(), message);
                throw new OrderServiceException(ErrorCode.INSUFFICIENT_STOCK);
            }
        }
        return infos;
    }

    /**
     * Резервирует товары по одному. Если какой-то товар не удалось зарезервировать, моментально запускает компенсацию.
     */
    private void reserveItems(List<OrderItemRequestDto> items, UUID sagaId) {
        List<OrderItemRequestDto> reservedItems = new ArrayList<>();
        for (OrderItemRequestDto item : items) {
            try {
                inventoryClient.reserve(item.productId(), item.quantity(), sagaId);
            } catch (Exception e) {
                log.error("Failed to reserved product {}, starting partial compensation ", item.productId());
                compensateReservations(reservedItems, sagaId);
                updateSagaState(sagaId, "RESERVATION_FAILED");
                throw new OrderServiceException(ErrorCode.RESERVATION_FAILED);
            }
        }
    }

    /**
     * Полная компенсация саги - освобождаем все товары
     */

    private void compensate(UUID sagaId, List<OrderItemRequestDto> items) {
        compensateReservations(items, sagaId);
    }

    /**
     * Освобождает резервы для указанного списка товаров
     */

    private void compensateReservations(List<OrderItemRequestDto> items, UUID sagaId) {
        for (OrderItemRequestDto item : items) {
            try {
                inventoryClient.release(item.productId(), item.quantity(), sagaId);
            } catch (Exception e) {
                log.error("Failed to release product {} during compensation", item.productId());
            }
        }
    }

    private void saveSagaLog(UUID sagaId, String state, OrderCreateRequestDto request) {
        SagaLogEntity logEntity = new SagaLogEntity();
        logEntity.setSagaId(sagaId);
        logEntity.setSagaType("CREATE_ORDER");
        logEntity.setCurrentState(state);
        try {
            logEntity.setPayload(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize to payload", e);
        }
        logEntity.setCreatedAt(LocalDateTime.now());
        logEntity.setUpdatedAt(LocalDateTime.now());
        sagaRepository.save(logEntity);
    }

    private void updateSagaState(UUID sagaId, String newState) {
        SagaLogEntity logEntity = sagaRepository.findById(sagaId)
                .orElseThrow(() -> new RuntimeException("Saga log not found"));
        logEntity.setCurrentState(newState);
        logEntity.setUpdatedAt(LocalDateTime.now());
        sagaRepository.save(logEntity);
    }

    private UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new OrderServiceException(ErrorCode.USER_NOT_FOUND));
    }

    private OrderEntity buildOrder(UserEntity user, List<OrderItemRequestDto> items,
                                   List<ProductInfo> productInfos) {
        OrderEntity order = new OrderEntity();
        order.setCustomerId(user.getId());
        order.setOrderStatus(OrderStatus.CREATED);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemEntity> itemEntities = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            OrderItemRequestDto itemRequest = items.get(i);
            ProductInfo info = productInfos.get(i);

            OrderItemEntity item = orderItemMapper.toEntity(itemRequest);
            BigDecimal price = BigDecimal.valueOf(info.getPrice());
            BigDecimal discount = BigDecimal.valueOf(info.getDiscount());
            item.setPrice(price);
            item.setDiscount(discount);

            BigDecimal itemTotal = price.subtract(discount)
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setTotalAmount(itemTotal);
            item.setOrder(order);
            itemEntities.add(item);

            totalAmount = totalAmount.add(itemTotal);
        }
        order.setTotalAmount(totalAmount);
        order.setItems(itemEntities);
        return order;
    }

    private OrderCreatedEvent buildOrderCreatedEvent(OrderEntity order, List<ProductInfo> productInfos) {
        ProductInfo product = productInfos.get(0);
        OrderItemEntity firstItem = order.getItems().get(0);
        return OrderCreatedEvent.builder()
                .orderId(order.getOrderId())
                .userId(order.getCustomerId())
                .productId(product.getId())
                .quantity(firstItem.getQuantity())
                .price(firstItem.getPrice())
                .discount(firstItem.getDiscount())
                .totalAmount(order.getTotalAmount())
                .status(order.getOrderStatus().name())
                .createdAt(LocalDateTime.now())
                .build();

    }

    private OrderDto buildOrderDto(OrderEntity savedOrder, List<ProductInfo> productInfos) {
        List<OrderItemDto> itemDtos = IntStream.range(0, savedOrder.getItems().size())
                .mapToObj(i -> {
                    OrderItemEntity item = savedOrder.getItems().get(i);
                    ProductInfo info = productInfos.get(i);
                    return OrderItemDto.builder()
                            .productId(item.getProductId())
                            .productName(info.getProductName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .discount(item.getDiscount())
                            .totalAmount(item.getTotalAmount())
                            .build();
                })
                .toList();
        return OrderDto.builder()
                .orderId(savedOrder.getOrderId())
                .customerId(savedOrder.getCustomerId())
                .totalAmount(savedOrder.getTotalAmount())
                .orderStatus(savedOrder.getOrderStatus())
                .items(itemDtos)
                .build();
    }
}
