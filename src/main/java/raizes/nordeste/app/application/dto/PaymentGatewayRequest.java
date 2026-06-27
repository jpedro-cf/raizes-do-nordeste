package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.Order;
import raizes.nordeste.app.domain.entities.PaymentMethod;

public record PaymentGatewayRequest(
        Long orderId,
        Order order,
        PaymentMethod method
) {}