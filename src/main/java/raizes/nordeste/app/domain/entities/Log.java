package raizes.nordeste.app.domain.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "logs")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Log {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String endpoint;

    @Column(name = "request_args", nullable = false, columnDefinition = "TEXT")
    private String requestArgs;

    @Column(nullable = false)
    private Instant timestamp = Instant.now();
}
