package orderservice.domain.service;

import http.order.OrderCreateRequestDto;
import http.order.OrderDto;
import orderservice.domain.entity.OrderEntity;

import java.util.UUID;

public interface OrderProcessor {
    OrderDto createOrder(OrderCreateRequestDto request);

    OrderEntity getOrderOrThrow(Long id);
}
