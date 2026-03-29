package orderservice.domain.service.users;

import orderservice.api.dto.*;
import orderservice.domain.entity.UserEntity;
import orderservice.domain.secuity.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserAuthProcessor {
    UserEntity registerUser(RegisterUserRequestDto request);

    JwtTokenResponseDto loginUser(LoginUserRequestDto request);

    UserDto getCurrentUser(UserDetailsImpl userDetails);

    JwtTokenResponseDto generateRefreshToken(RefreshTokenRequestDto request, UserDetailsImpl userDetails);
}
