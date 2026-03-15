package http.order;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemDto(Long productId,
                           String productName,
                           Integer quantity,
                           BigDecimal price,
                           BigDecimal discount,
                           BigDecimal totalAmount) {
}