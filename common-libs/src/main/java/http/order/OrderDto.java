package http.order;


import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderDto(Long orderId,
                       Long customerId,
                       BigDecimal totalAmount,
                       OrderStatus orderStatus,
                       List<OrderItemDto> items) {
}