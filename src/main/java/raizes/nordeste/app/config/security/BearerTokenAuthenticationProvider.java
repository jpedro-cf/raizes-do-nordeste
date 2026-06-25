package raizes.nordeste.app.config.security;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import raizes.nordeste.app.application.TokenService;
import raizes.nordeste.app.infra.repositories.UsersRepository;
import raizes.nordeste.app.shared.exceptions.NotFoundException;

@Component
@RequiredArgsConstructor
public class BearerTokenAuthenticationProvider implements AuthenticationProvider {
    private final UsersRepository usersRepository;
    private final TokenService tokenService;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            var bearerAuth = (BearerTokenAuthentication) authentication;

            var decoded = tokenService.decode(bearerAuth.getToken());
            if(decoded.get("user_id") == null) {
                throw new Exception("User id not found in token.");
            }

            var userId = (String) decoded.get("user_id");

            var user = usersRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new NotFoundException("User not found."));

            bearerAuth.setAuthenticated(true);
            bearerAuth.setUser(user);

            return bearerAuth;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthentication.class.equals(authentication);
    }
}
