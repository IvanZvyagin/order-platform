package inventoryservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "product_name", nullable = false)
    private String productName;


    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;


    @Column(name = "discount", nullable = false, precision = 5, scale = 2)
    private BigDecimal discount;


    @Column(name = "created_at", nullable = false)
    private Instant createdAt;


    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = Instant.now();
    }

    public BigDecimal getPriceWithDiscount(){
        return price.subtract(discount);
    }

}
