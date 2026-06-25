package raizes.nordeste.app.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.Instant;

@Entity
@Table(name = "product_units",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "unit_id"}))
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProductUnit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private BigInteger price;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
