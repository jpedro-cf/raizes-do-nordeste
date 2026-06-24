package raizes.nordeste.app.infra.repositories.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raizes.nordeste.app.application.Hasher;

@Component
public class PasswordHasher implements Hasher {
    private final PasswordEncoder encoder;

    public PasswordHasher(PasswordEncoder encoder){
        this.encoder = encoder;
    }

    @Override
    public String hash(String value) {
        return encoder.encode(value);
    }

    @Override
    public boolean verify(String hash, String value) {
        return encoder.matches(value, hash);
    }
}
