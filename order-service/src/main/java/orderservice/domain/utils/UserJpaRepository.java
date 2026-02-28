package orderservice.domain.utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import orderservice.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByUsername(@NotBlank @Size(min = 3, max = 50) String username);

    Optional <UserEntity> findByUsername(String username);
}