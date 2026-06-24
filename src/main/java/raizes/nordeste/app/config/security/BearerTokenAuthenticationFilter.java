package raizes.nordeste.app.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import raizes.nordeste.app.shared.exceptions.UnauthorizedException;

import java.io.IOException;

public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager manager;

    public BearerTokenAuthenticationFilter(AuthenticationManager manager){
        this.manager = manager;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            var token = header.substring(7);

            var authentication = new BearerTokenAuthentication(token);

            var authenticated = manager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token or it was not provided in the request.");
        }
        filterChain.doFilter(request, response);
    }
}
