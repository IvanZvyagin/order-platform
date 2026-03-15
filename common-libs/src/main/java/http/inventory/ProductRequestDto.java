package http.inventory;

import java.math.BigDecimal;

public record ProductRequestDto(String productName,
                                Integer quantity,
                                BigDecimal price,
                                BigDecimal discount)
{}