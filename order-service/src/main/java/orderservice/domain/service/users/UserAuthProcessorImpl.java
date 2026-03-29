package orderservice.domain.service.users;

import exception.OrderServiceException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import orderservice.api.dto.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserDetails userDetails;

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
        String accessToken = jwtUtils.generateAccessToken(request.username());
        String refreshToken = jwtUtils.generateRefreshToken(request.username());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

//        String role = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .findFirst()
//                .orElse("USER")
//                .replace("ROLE_","");
        return JwtTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(userDetails.getUsername())
                .userRole(String.valueOf(getUserRole(userDetails)))
                .build();

    }

    public JwtTokenResponseDto generateRefreshToken(RefreshTokenRequestDto request, UserDetailsImpl userDetails){
        String refreshToken = request.refreshToken();
        if(!jwtUtils.validateJwtToken(refreshToken)){
            throw new OrderServiceException(OrderServiceException.ErrorCode.UNAUTHORIZED, "Invalid refresh token");
        }
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        String newAccessToken = jwtUtils.generateAccessToken(username);
        String newRefreshToken = jwtUtils.generateRefreshToken(username);
        return JwtTokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .username(username)
                .userRole(String.valueOf(getUserRole(userDetails)))
                .build();
    }

    @Override
    public UserDto getCurrentUser(UserDetailsImpl userDetails) {
        if(userDetails ==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return UserDto.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .userRole(getUserRole(userDetails))
                .build();
    }

    private UserRole getUserRole(UserDetails  userDetails){
       return UserRole.valueOf(userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_",""))
                .findFirst()
                .orElse("USER"));
    }

    private void validateUsername(@NotBlank
                                  @Size(min = 3, max = 50)
                                  String username) {
        if(userJpaRepository.existsByUsername(username)){
            throw  new RuntimeException("Username is already taken" + username);
        }
    }
}
