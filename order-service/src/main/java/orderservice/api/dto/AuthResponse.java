package orderservice.api.dto;

public record AuthResponse(
        String accesToken,
        String refreshToken,
        String tokenType
) {
}
