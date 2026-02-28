package orderservice.api.dto;

import orderservice.domain.entity.OrderItemEntity;

import java.math.BigDecimal;

/**
 * DTO for {@link OrderItemEntity}
 */
public record OrderItemDto(Long id, Long itemId, Integer quantity, BigDecimal price, Integer discount) {
}