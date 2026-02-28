package orderservice.domain;

import java.math.BigDecimal;

/**
 * DTO for {@link OrderItemEntity}
 */
public record OrderItemDto(Long id, Long itemId, Integer quantity, BigDecimal price, Integer discount) {
}