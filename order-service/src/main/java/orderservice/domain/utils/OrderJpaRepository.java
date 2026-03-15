package orderservice.domain.utils;

import orderservice.domain.entity.OrderEntity;
import http.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    Optional<OrderEntity> findByOrderId(UUID orderId);

    List<OrderEntity> findByCustomerId(Long customerId);

    List<OrderEntity> findByOrderStatus(OrderStatus status);

}