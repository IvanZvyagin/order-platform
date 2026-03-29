package orderservice.domain.saga;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SagaLogJpaRepository extends JpaRepository<SagaLogEntity, UUID> {
    @Override
    Optional<SagaLogEntity> findById(@NonNull UUID sagaId);
}
