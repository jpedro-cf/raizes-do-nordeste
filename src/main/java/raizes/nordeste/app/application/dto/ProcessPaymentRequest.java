package raizes.nordeste.app.application.dto;

import jakarta.validation.constraints.NotNull;
import raizes.nordeste.app.config.validation.ValidEnum;
import raizes.nordeste.app.domain.entities.OrderStatus;
import raizes.nordeste.app.domain.entities.PaymentMethod;

public record ProcessPaymentRequest(
        @NotNull Long orderId,
        @NotNull
        @ValidEnum(enumClass = PaymentMethod.class,
            message = "Invalid value. Accepted values: CARD, PIX, CASH")
        String paymentMethod
) {}