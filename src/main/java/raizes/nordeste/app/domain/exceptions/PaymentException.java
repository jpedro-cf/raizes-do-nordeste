package raizes.nordeste.app.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import raizes.nordeste.app.shared.exceptions.AppException;

public class PaymentException extends AppException {
    public PaymentException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problem.setTitle("PAYMENT_ERROR");
        problem.setDetail(getMessage());

        return problem;
    }
}
