package orderservice.domain.utils;

import orderservice.domain.entity.OrderEntity;
import orderservice.domain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findById(Long id);

    List<OrderEntity> findByCustomerId(Long customerId);

    List<OrderEntity> findByOrderStatus(OrderStatus status);

}