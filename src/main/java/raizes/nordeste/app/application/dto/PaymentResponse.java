package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.Payment;
import raizes.nordeste.app.domain.entities.PaymentStatus;

import java.time.Instant;

public record PaymentResponse(
        Long id,
        Long orderId,
        Long userId,
        PaymentStatus status,
        Instant timestamp) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getOrder().getUser().getId(),
                payment.getStatus(),
                payment.getTimestamp());
    }
}
