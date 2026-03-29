package orderservice.api.dto;

import lombok.Builder;
import orderservice.domain.entity.UserRole;

/**
 * DTO for {@link orderservice.domain.entity.UserEntity}
 */
@Builder
public record UserDto(Long id,
                      String username,
                      String email,
                      UserRole userRole) {
}