package repository;

import entity.NsOrderEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface NsOrderRepository extends R2dbcRepository<NsOrderEntity, Long> {

    Mono<NsOrderEntity> findByOrderId(Long id);

    Flux<NsOrderEntity> findAllByUserId(Long userId);

    Flux<NsOrderEntity> findByStatus(String status);

    @Query("SELECT * FROM orders WHERE created_at BETWEEN $1 and $2 ORDER BY created_at DESC ")
    Flux<NsOrderEntity> findOrdersInPeriod(LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT user_id, COUNT(*) as order_count, SUM(total_price) as total_spent 
        FROM orders 
        GROUP BY user_id
        """)
    Flux<UserStats> getUserStatistic();

    @Query("""
        SELECT product_id, COUNT(*) as order_count, SUM(quantity) as total_quantity, SUM(total_amount) a
        FROM orders
        GROUP BY user_id
""")
    Flux<ProductStats> getProductsStatistic();

    @Getter
    @Setter
    class UserStats{
        private Long userId;
        private Long orderCount;
        private BigDecimal totalSpent;
    }

    @Getter
    @Setter
    class ProductStats{
        private Long productId;
        private Long orderCount;
        private Long totalQuantity;
        private BigDecimal totalRevenue;
    }
}
