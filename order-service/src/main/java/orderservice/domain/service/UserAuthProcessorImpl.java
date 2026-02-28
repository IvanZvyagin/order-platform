package orderservice.domain.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import orderservice.api.dto.RegisterUserRequestDto;
import orderservice.domain.entity.UserEntity;
import orderservice.domain.utils.UserEntityMapper;
import orderservice.domain.utils.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAuthProccesorImpl implements UserAuthProccesor {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(RegisterUserRequestDto request) {
        validateUsername(request.username());
        var entity = userEntityMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userJpaRepository.save(entity);
    }

    private void validateUsername(@NotBlank
                                  @Size(min = 3, max = 50)
                                  String username) {
        if(userJpaRepository.existsByUsername(username)){
            throw  new RuntimeException("Username is already taken" + username);
        }
    }
}
