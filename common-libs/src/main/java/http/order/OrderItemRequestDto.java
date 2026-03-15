package http.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderItemRequestDto(
        @NotNull(message = "Product ID is  required")
        Long productId,
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {}
