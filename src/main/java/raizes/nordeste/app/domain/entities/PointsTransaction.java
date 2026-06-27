package raizes.nordeste.app.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "points_transactions")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class PointsTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointsTransactionType type;

    @Column(nullable = false)
    private Long points;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}