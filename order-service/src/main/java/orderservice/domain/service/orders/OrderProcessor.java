package orderservice.domain.service.orders;

import http.order.OrderCreateRequestDto;
import http.order.OrderDto;
import orderservice.domain.entity.OrderEntity;

public interface OrderProcessor {
    OrderDto createOrder(OrderCreateRequestDto request);

    OrderEntity getOrderOrThrow(Long id);
}
