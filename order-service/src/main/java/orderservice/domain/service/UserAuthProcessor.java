package orderservice.domain.service;

import orderservice.api.dto.RegisterUserRequestDto;
import orderservice.domain.entity.UserEntity;

public interface UserAuthProccesor {
    UserEntity registerUser(RegisterUserRequestDto request);
}
