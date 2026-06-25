package raizes.nordeste.app.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import raizes.nordeste.app.application.TokenService;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtDecoder decoder;
    private final JwtEncoder encoder;

    @Override
    public String encode(Map<String, String> data, long expiration) {
        Instant now = Instant.now();

        var header = JwsHeader.with(MacAlgorithm.HS256).build();

        var claims = JwtClaimsSet.builder()
                .issuer("raizes_do_nordeste")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofSeconds(expiration)))
                .subject(data.get("subject"))
                .claims(c -> data.forEach((key, value) -> {
                    if (!key.equals("subject")) c.put(key, value);
                }))
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Override
    public Map<String, Object> decode(String value) {
        Jwt decoded = decoder.decode(value);

        return decoded.getClaims();
    }
}
