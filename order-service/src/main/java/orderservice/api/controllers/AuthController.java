package orderservice.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.api.dto.*;
import orderservice.domain.secuity.UserDetailsImpl;
import orderservice.domain.service.users.UserAuthProcessor;
import orderservice.domain.utils.UserEntityMapperProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserAuthProcessor userAuthProcessor;
    private final UserEntityMapperProcessor userEntityMapper;

    @PostMapping("/register")
    public UserDto register(
            @Valid
            @RequestBody RegisterUserRequestDto request
    )
    {
        log.info("Register new user {}",request);
        return userEntityMapper.toUserDto(userAuthProcessor.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDto> login(
            @Valid
            @RequestBody LoginUserRequestDto request
            )
    {
        log.info("User login completed {}", request);
        return ResponseEntity.ok(userAuthProcessor.loginUser(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Получение данных о пользователе")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserDto> getCurrentUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(userAuthProcessor.getCurrentUser(userDetails));
    }
}
