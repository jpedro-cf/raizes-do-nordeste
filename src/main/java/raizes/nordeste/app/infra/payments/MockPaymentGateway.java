package raizes.nordeste.app.infra.payments;

import org.springframework.stereotype.Component;
import raizes.nordeste.app.application.PaymentGateway;
import raizes.nordeste.app.application.dto.PaymentGatewayRequest;
import raizes.nordeste.app.application.dto.PaymentGatewayResponse;
import raizes.nordeste.app.domain.entities.PaymentMethod;

import java.math.BigInteger;

@Component
public class MockPaymentGateway implements PaymentGateway {
    private final BigInteger CARD_LIMIT = BigInteger.valueOf(10000);

    @Override
    public PaymentGatewayResponse process(PaymentGatewayRequest request) {
        // Simular caso de erro.
        var reachedLimit = request.order().getTotal().compareTo(CARD_LIMIT) > 0;
        if(request.method().equals(PaymentMethod.CARD) && reachedLimit) {
            return new PaymentGatewayResponse(false,
                    "The limit of CARD transactions is R$100,00.");
        }

        return new PaymentGatewayResponse(true, "Payment processed successfully.");
    }
}