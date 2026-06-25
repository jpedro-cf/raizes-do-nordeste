package raizes.nordeste.app.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.CUSTOMER;

    @Column(nullable = true)
    private String phone;
    @Column(nullable = true)
    private Integer age;

    @Column(name = "lgpd_consent", nullable = false)
    private boolean lgpdConsent = false;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
