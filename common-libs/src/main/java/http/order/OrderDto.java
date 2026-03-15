package http.order;


import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderDto(UUID orderId,
                       Long customerId,
                       BigDecimal totalAmount,
                       OrderStatus orderStatus,
                       List<OrderItemDto> items) {
}