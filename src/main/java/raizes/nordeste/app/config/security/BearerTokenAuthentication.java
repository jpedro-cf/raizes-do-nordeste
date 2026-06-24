package raizes.nordeste.app.config.security;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import raizes.nordeste.app.domain.entities.User;

import java.util.Collection;
import java.util.List;

@Getter @Setter
public class BearerTokenAuthentication implements Authentication {
    private final String token;
    private User user;
    private boolean authenticated;

    public BearerTokenAuthentication(String token){
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().getValue()));
    }

    @Override
    public @Nullable Object getCredentials() {
        return token;
    }

    @Override
    public User getDetails() {
        return user;
    }

    @Override
    public Long getPrincipal() {
        return user.getId();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
    @Override
    public String getName() {
        return "BearerTokenAuthentication";
    }
}
