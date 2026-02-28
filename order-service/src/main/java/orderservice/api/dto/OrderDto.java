package orderservice.api;

import orderservice.domain.OrderEntity;
import orderservice.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for {@link OrderEntity}
 */
public record OrderDto(Long id, Long customerId, BigDecimal totalAmount, OrderStatus orderStatus,
                       Set<OrderItemDto> items) {
}