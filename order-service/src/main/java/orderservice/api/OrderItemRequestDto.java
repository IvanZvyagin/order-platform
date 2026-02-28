package orderservice.api;

public record OrderItemRequestDto(
        Long itemId,
        Integer quantity,
        String itemName
) {}
