package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.User;
import raizes.nordeste.app.domain.entities.UserRole;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String email,
        Long points,
        boolean lgpdConsent,
        Integer age,
        String phone,
        String role,
        Instant createdAt
) {
    public static UserResponse from(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPoints(),
                user.getLgpdConsent(),
                user.getAge(),
                user.getPhone(),
                user.getRole().getValue().toUpperCase(),
                user.getCreatedAt()
        );
    }
}
