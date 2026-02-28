package orderservice.domain.secuity;

import lombok.RequiredArgsConstructor;
import orderservice.domain.utils.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        return UserDetailsImpl.build(user);
    }
}
