package orderservice.api.dto;

import lombok.Builder;
import orderservice.domain.entity.UserRole;

import java.util.UUID;

/**
 * DTO for {@link orderservice.domain.entity.UserEntity}
 */
@Builder
public record UserDto(UUID id,
                      String username,
                      UserRole userRole) {
}