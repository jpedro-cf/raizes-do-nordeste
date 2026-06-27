package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.Payment;
import raizes.nordeste.app.domain.entities.PaymentStatus;

import java.math.BigInteger;
import java.time.Instant;

public record PaymentResponse(
        Long id,
        BigInteger total,
        Long orderId,
        Long userId,
        PaymentStatus status,
        Long pointsEarned,
        Instant timestamp) {

    public static PaymentResponse from(Payment payment) {
        var points = payment.getOrder().getTotal()
                .divide(BigInteger.valueOf(100))
                .longValue();
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getTotal(),
                payment.getOrder().getId(),
                payment.getOrder().getUser().getId(),
                payment.getStatus(),
                points <= 0 || payment.getStatus().equals(PaymentStatus.REJECTED) ? 0 : points,
                payment.getTimestamp());
    }
}
