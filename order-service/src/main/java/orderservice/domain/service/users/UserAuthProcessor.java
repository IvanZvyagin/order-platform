package orderservice.domain.service.users;

import orderservice.api.dto.JwtTokenResponseDto;
import orderservice.api.dto.LoginUserRequestDto;
import orderservice.api.dto.RegisterUserRequestDto;
import orderservice.api.dto.UserDto;
import orderservice.domain.entity.UserEntity;
import orderservice.domain.secuity.UserDetailsImpl;

public interface UserAuthProcessor {
    UserEntity registerUser(RegisterUserRequestDto request);

    JwtTokenResponseDto loginUser(LoginUserRequestDto request);

    UserDto getCurrentUser(UserDetailsImpl userDetails);
}
