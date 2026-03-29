package orderservice.domain.service.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import orderservice.api.dto.JwtTokenResponseDto;
import orderservice.api.dto.LoginUserRequestDto;
import orderservice.api.dto.RegisterUserRequestDto;
import orderservice.api.dto.UserDto;
import orderservice.domain.entity.UserEntity;
import orderservice.domain.entity.UserRole;
import orderservice.domain.secuity.JwtUtils;
import orderservice.domain.secuity.UserDetailsImpl;
import orderservice.domain.utils.UserEntityMapperProcessor;
import orderservice.domain.utils.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
@Transactional
public class UserAuthProcessorImpl implements UserAuthProcessor {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapperProcessor userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public UserEntity registerUser(RegisterUserRequestDto request) {
        validateUsername(request.username());
        request.isPasswordsMatch();
        var user = userEntityMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        return userJpaRepository.save(user);
    }

    @Override
    public JwtTokenResponseDto loginUser(LoginUserRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateAccessToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER")
                .replace("ROLE_","");
        return JwtTokenResponseDto.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .userRole(role)
                .build();

    }

    @Override
    public UserDto getCurrentUser(UserDetailsImpl userDetails) {
        if(userDetails ==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        String role = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_",""))
                .findFirst()
                .orElse("USER");

        return UserDto.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .userRole(UserRole.USER)
                .build();
    }

    private void validateUsername(@NotBlank
                                  @Size(min = 3, max = 50)
                                  String username) {
        if(userJpaRepository.existsByUsername(username)){
            throw  new RuntimeException("Username is already taken" + username);
        }
    }
}
