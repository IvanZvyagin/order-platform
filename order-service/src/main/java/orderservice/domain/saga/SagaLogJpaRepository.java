package orderservice.domain.saga;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SagaLogJpaRepository extends JpaRepository<SagaLogEntity, UUID> {
    Optional<SagaLogEntity> findById(UUID sagaId);
}
