package orderservice.api.dto;

import java.util.Set;

public record OrderCreateRequestDto(
        Set<OrderItemRequestDto> items,
        Long customerId
) {}
