package orderservice.api.dto;

import lombok.Builder;

@Builder
public record JwtTokenResponseDto(
        String accessToken,
        String refreshToken,
        String username,
        String userRole
) {}
