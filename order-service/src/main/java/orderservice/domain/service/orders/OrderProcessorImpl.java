package orderservice.domain.service.orders;

import exception.ErrorCode;
import exception.OrderServiceException;
import http.order.OrderCreateRequestDto;
import http.order.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.domain.entity.OrderEntity;
import orderservice.domain.saga.CreateOrderSagaOrchestrator;
import orderservice.domain.utils.OrderJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProcessorImpl implements OrderProcessor {
    private final CreateOrderSagaOrchestrator sagaOrchestrator;
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderDto createOrder(OrderCreateRequestDto request) {
        return sagaOrchestrator.createOrderSaga(request);
    }

    @Override
    public OrderEntity getOrderOrThrow(Long id) {
        Optional<OrderEntity> orderEntityOptional = orderJpaRepository.findById(id);
        return orderEntityOptional
                .orElseThrow(() ->
                        new OrderServiceException(ErrorCode.ORDER_NOT_FOUND,
                                "Entity with id `%s` not found".formatted(id)));
    }
}