package orderservice.api.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        Long productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal discount,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
) {
}
