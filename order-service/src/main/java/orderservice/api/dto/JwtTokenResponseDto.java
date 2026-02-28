package orderservice.api.dto;

import lombok.Builder;

@Builder
public record JwtTokenResponseDto(
        String token,
        String username,
        String userRole
) {}
