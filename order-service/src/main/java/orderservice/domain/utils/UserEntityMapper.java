package orderservice.domain.utils;

import orderservice.api.dto.RegisterUserRequestDto;
import orderservice.api.dto.UserDto;
import orderservice.domain.entity.UserEntity;

public interface UserEntityMapper {

    UserEntity toEntity(RegisterUserRequestDto request);

    UserDto toUserDto(UserEntity userEntity);
}
