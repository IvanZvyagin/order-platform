package entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("orders")
public class NsOrderEntity {
    @Id
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("user_id")
    private Long userId;

    @Column("product_id")
    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal discount;

    @Column("total_amount")
    private BigDecimal totalAmount;

    private String status;

    @Column("created_at")
    private LocalDateTime createdAt;
}
