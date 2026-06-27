package raizes.nordeste.app.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raizes.nordeste.app.application.PaymentGateway;
import raizes.nordeste.app.application.dto.PaymentGatewayRequest;
import raizes.nordeste.app.application.dto.PaymentResponse;
import raizes.nordeste.app.application.dto.ProcessPaymentRequest;
import raizes.nordeste.app.domain.entities.OrderStatus;
import raizes.nordeste.app.domain.entities.Payment;
import raizes.nordeste.app.domain.entities.PaymentMethod;
import raizes.nordeste.app.domain.entities.PaymentStatus;
import raizes.nordeste.app.domain.exceptions.OrderStatusInvalidException;
import raizes.nordeste.app.domain.exceptions.PaymentException;
import raizes.nordeste.app.infra.repositories.OrdersRepository;
import raizes.nordeste.app.infra.repositories.PaymentsRepository;
import raizes.nordeste.app.shared.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentsService {
    private final PaymentsRepository paymentsRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentGateway paymentGateway;
    private final LoyaltyService loyaltyService;

    @Transactional(dontRollbackOn = {PaymentException.class})
    public PaymentResponse process(ProcessPaymentRequest request) {
        var order = ordersRepository.findById(request.orderId())
                .orElseThrow(() -> new NotFoundException("Order with this id not found."));

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new OrderStatusInvalidException("You can only request a payment for a PENDING order.");
        }

        var gatewayRequest = new PaymentGatewayRequest(
                order.getId(),
                order,
                PaymentMethod.valueOf(request.paymentMethod().toUpperCase())
        );

        var gatewayResponse = paymentGateway.process(gatewayRequest);

        var payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(PaymentMethod.valueOf(request.paymentMethod().toUpperCase()));
        payment.setAmount(order.getTotal());

        if (!gatewayResponse.approved()) {
            payment.setStatus(PaymentStatus.REJECTED);
            order.setStatus(OrderStatus.CANCELLED);
            paymentsRepository.save(payment);

            throw new PaymentException(gatewayResponse.message());
        }

        payment.setStatus(PaymentStatus.APPROVED);
        order.setStatus(OrderStatus.CONFIRMED);
        loyaltyService.earnPoints(order);

        return PaymentResponse.from(paymentsRepository.save(payment));
    }

    public Page<PaymentResponse> getAll(Pageable pageable){
        return paymentsRepository.findAll(pageable)
                .map(PaymentResponse::from);
    }
}