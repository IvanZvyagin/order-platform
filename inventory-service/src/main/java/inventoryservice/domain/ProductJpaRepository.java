package inventoryservice.domain;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findById(@NonNull Long id);

    boolean existsByProductName(String productName);
}