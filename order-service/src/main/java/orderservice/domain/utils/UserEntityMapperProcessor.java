package orderservice.domain.utils;

import orderservice.api.dto.RegisterUserRequestDto;
import orderservice.api.dto.UserDto;
import orderservice.domain.entity.UserEntity;
import orderservice.domain.entity.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapperProcessor implements UserEntityMapper {
    @Override
    public UserEntity toEntity(RegisterUserRequestDto request) {
        if (request == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(request.username());
        userEntity.setPassword(request.password());
        userEntity.setEmail(request.email());
        userEntity.setUserRole(UserRole.USER);

        return userEntity;
    }

    @Override
    public UserDto toUserDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();
        userDto.id(userEntity.getId());
        userDto.username(userEntity.getUsername());
        userDto.email(userEntity.getEmail());
        userDto.userRole(userEntity.getUserRole());

        return userDto.build();
    }
}
