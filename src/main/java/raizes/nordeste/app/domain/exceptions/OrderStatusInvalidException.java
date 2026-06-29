package raizes.nordeste.app.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import raizes.nordeste.app.api.exceptions.AppException;

public class OrderStatusInvalidException extends AppException {
    public OrderStatusInvalidException() {
        super("The status of the order is invalid for this operation.");
    }

    public OrderStatusInvalidException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("ORDER_STATUS_INVALID");
        problem.setDetail(getMessage());

        return problem;
    }
}