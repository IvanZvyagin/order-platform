package orderservice.domain.service.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.api.dto.OrderCreatedEvent;
import orderservice.domain.entity.OrderEntity;
import orderservice.domain.entity.OutboxEventEntity;
import orderservice.domain.utils.OrderJpaRepository;
import orderservice.domain.utils.OutboxJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPersistenceServiceImpl implements OrderStatusPersistenceService {
    private final OrderJpaRepository orderRepository;
    private final OutboxJpaRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OrderEntity saveOrderWithOutbox(OrderEntity order, OrderCreatedEvent event){
        OrderEntity saveOrder = orderRepository.save(order);
        log.info("Order saved: {}", saveOrder.getOrderId());
        OrderCreatedEvent finalEvent = new OrderCreatedEvent(
                saveOrder.getOrderId(),
                event.userId(),
                event.productId(),
                event.quantity(),
                event.price(),
                event.discount(),
                event.totalAmount(),
                event.status(),
                event.createdAt()
        );

        OutboxEventEntity outbox = OutboxEventEntity.builder()
                .id(UUID.randomUUID())
                .aggregateType("ORDER")
                .aggregateId(saveOrder.getOrderId().toString())
                .eventType("OrderCreated")
                .payload(toJson(finalEvent))
                .createdAt(LocalDateTime.now())
                .status("PENDING")
                .build();
        outboxRepository.save(outbox);
        log.info("Outbox event saved for order: {}", saveOrder.getOrderId());
        return saveOrder;
    }

    private String toJson(OrderCreatedEvent event){
        try {
            return objectMapper.writeValueAsString(event);
        }catch (Exception e){
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
