package raizes.nordeste.app.application;

import raizes.nordeste.app.application.dto.PaymentGatewayRequest;
import raizes.nordeste.app.application.dto.PaymentGatewayResponse;

public interface PaymentGateway {
    PaymentGatewayResponse process(PaymentGatewayRequest request);
}