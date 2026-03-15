package orderservice.domain.service;

import http.order.OrderCreateRequestDto;
import http.order.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.domain.entity.OrderEntity;
import orderservice.domain.saga.CreateOrderSagaOrchestrator;
import orderservice.domain.utils.OrderJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

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
    public OrderEntity getOrderOrThrow(UUID id) {
        Optional<OrderEntity> orderEntityOptional = orderJpaRepository.findById(id);
        return orderEntityOptional
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Entity with id `%s` not found".formatted(id)));
    }
//  private final OrderKafkaProducer orderKafkaProducer;
}