package http.inventory;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponseDto(Long id,
                                 String productName,
                                 Integer quantity,
                                 BigDecimal price,
                                 BigDecimal discount,
                                 Instant createdAt,
                                 Instant updatedAt)
{}