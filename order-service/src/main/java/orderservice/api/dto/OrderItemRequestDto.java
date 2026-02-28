package orderservice.api.dto;

public record OrderItemRequestDto(
        Long itemId,
        Integer quantity,
        String itemName
) {}
