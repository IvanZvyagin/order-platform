package http.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderCreateRequestDto(
        @NotEmpty(message = "Order items cannot be empty")
        List<OrderItemRequestDto> items
) {}
