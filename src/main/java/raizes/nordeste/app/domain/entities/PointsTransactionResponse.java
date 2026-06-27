package raizes.nordeste.app.domain.entities;

import java.time.Instant;

public record PointsTransactionResponse(
        Long id,
        PointsTransactionType type,
        Long points,
        Long orderId,
        Long userId,
        Instant createdAt
) {
    public static PointsTransactionResponse from(PointsTransaction t) {
        assert t.getOrder() != null;
        return new PointsTransactionResponse(
                t.getId(),
                t.getType(),
                t.getPoints(),
                t.getOrder().getId(),
                t.getOrder().getUser().getId(),
                t.getCreatedAt()
        );
    }
}