package orderservice.domain.secuity;

import exception.ErrorCode;
import exception.OrderServiceException;
import lombok.RequiredArgsConstructor;
import orderservice.domain.utils.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new OrderServiceException(ErrorCode.USER_NOT_FOUND));
        return UserDetailsImpl.build(user);
    }
}
