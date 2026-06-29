package raizes.nordeste.app.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import raizes.nordeste.app.application.Hasher;
import raizes.nordeste.app.domain.entities.User;
import raizes.nordeste.app.domain.entities.UserRole;
import raizes.nordeste.app.infra.repositories.UsersRepository;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
    private final UsersRepository usersRepository;
    private final Hasher hasher;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (usersRepository.findByEmail("admin@email.com").isPresent()) return;

        var user = User.builder()
                .name("ADMIN")
                .email("admin@email.com")
                .password(hasher.hash(adminPassword))
                .phone(null)
                .age(null)
                .points(0L)
                .lgpdConsent(true)
                .role(UserRole.ADMIN)
                .createdAt(Instant.now())
                .build();

        usersRepository.save(user);
        System.out.println("ADMIN user created successfully.");
    }
}
