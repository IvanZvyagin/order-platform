package orderservice.domain.service;

import orderservice.api.dto.OrderCreateRequestDto;
import orderservice.domain.entity.OrderEntity;

public interface OrderProcessor {
    OrderEntity createOrder(OrderCreateRequestDto request);

    OrderEntity getOrderOrThrow(Long id);
}
