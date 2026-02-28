package orderservice.api;

import java.util.Set;

public record OrderCreateRequestDto(
        Set<OrderItemRequestDto> items,
        Long customerId
) {}
