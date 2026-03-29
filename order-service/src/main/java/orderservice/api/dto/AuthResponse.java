package orderservice.api.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
