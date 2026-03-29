package orderservice.domain.service.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.domain.entity.UserEntity;
import orderservice.domain.entity.UserRole;
import orderservice.domain.utils.UserJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if(userJpaRepository.findByUsername("admin").isEmpty()){
            UserEntity admin = UserEntity.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .userRole(UserRole.ADMIN)
                    .build();
            userJpaRepository.save(admin);
            log.info("User admin created");
        }
    }
}
