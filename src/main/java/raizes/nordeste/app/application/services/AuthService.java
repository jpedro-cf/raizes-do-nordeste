package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.application.Hasher;
import raizes.nordeste.app.application.TokenService;
import raizes.nordeste.app.application.dto.AuthResponse;
import raizes.nordeste.app.application.dto.LoginRequest;
import raizes.nordeste.app.application.dto.RegisterRequest;
import raizes.nordeste.app.domain.entities.User;
import raizes.nordeste.app.domain.entities.UserRole;
import raizes.nordeste.app.domain.exceptions.EmailInUseException;
import raizes.nordeste.app.infra.repositories.UsersRepository;
import raizes.nordeste.app.shared.exceptions.ForbiddenException;
import raizes.nordeste.app.shared.exceptions.InvalidArgumentException;
import raizes.nordeste.app.shared.exceptions.NotFoundException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepository usersRepository;
    private final TokenService tokenService;
    private final Hasher passwordHasher;

    @Transactional
    public AuthResponse register(RegisterRequest dto) {
        if(!dto.lgpdConsent()){
            throw new InvalidArgumentException("You must consent to LGPD.");
        }

        if (usersRepository.findByEmail(dto.email()).isPresent()) {
            throw new EmailInUseException();
        }

        var user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordHasher.hash(dto.password()))
                .phone(dto.phone())
                .age(dto.age())
                .points(0L)
                .lgpdConsent(true)
                .role(UserRole.CUSTOMER)
                .createdAt(Instant.now())
                .build();

        var savedUser = usersRepository.save(user);

        var token = tokenService.encode(Map.of(
                "subject", savedUser.getId().toString(),
                "user_id", savedUser.getId().toString(),
                "email", savedUser.getEmail()
        ), Duration.ofDays(1).getSeconds());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest dto) {
        var user = usersRepository.findByEmail(dto.email())
                .orElseThrow(() -> new NotFoundException("User with this email not found."));

        if (!passwordHasher.verify(user.getPassword(), dto.password())) {
            throw new ForbiddenException("E-mail or password incorrect.");
        }

        var token = tokenService.encode(Map.of(
                "subject", user.getId().toString(),
                "user_id", user.getId().toString(),
                "email", user.getEmail()
        ), Duration.ofDays(1).getSeconds());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }
}
