package orderservice.api.dto;

import orderservice.domain.entity.OrderEntity;
import orderservice.domain.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for {@link OrderEntity}
 */
public record OrderDto(Long id, Long customerId, BigDecimal totalAmount, OrderStatus orderStatus,
                       Set<OrderItemDto> items) {
}