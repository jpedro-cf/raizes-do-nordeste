package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.PointsTransactionResponse;

import java.util.List;

public record LoyaltyResponse(
        Long totalPoints,
        List<PointsTransactionResponse> history
) {
}
