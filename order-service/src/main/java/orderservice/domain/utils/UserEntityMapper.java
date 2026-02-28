package orderservice.domain.utils;

import orderservice.api.dto.RegisterUserRequestDto;
import orderservice.api.dto.UserDto;
import orderservice.domain.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {
    UserEntity toEntity(RegisterUserRequestDto request);

    UserDto toUserDto(UserEntity entity);
}