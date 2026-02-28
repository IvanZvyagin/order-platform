package orderservice.domain;

import orderservice.api.OrderCreateRequestDto;

public interface OrderProcessor {
    OrderEntity createOrder(OrderCreateRequestDto request);

    OrderEntity getOrderOrThrow(Long id);
}
