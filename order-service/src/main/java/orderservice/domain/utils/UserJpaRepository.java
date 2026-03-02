package orderservice.domain.utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import orderservice.domain.entity.UserEntity;
import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {


    Optional <UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(@NotBlank
                             @Size(min = 3, max = 50) String username);

    boolean existsByEmail(String email);
}
