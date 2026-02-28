package orderservice.api;

import orderservice.domain.OrderEntity;

public interface OrderProcessor {
    OrderEntity createOrder(OrderEntity orderEntity);

    OrderEntity getOrderOrThrow(Long id);
}
