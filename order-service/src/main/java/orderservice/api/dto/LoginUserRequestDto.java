package orderservice.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequestDto
        (
        @NotBlank
        String username,
        @NotBlank
        String password
) {}
