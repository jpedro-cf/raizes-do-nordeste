package raizes.nordeste.app.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import raizes.nordeste.app.api.exceptions.AppException;

public class ProductOutOfStockException extends AppException {
    public ProductOutOfStockException() {
        super("This product is unavailable or out of stock.");
    }

    public ProductOutOfStockException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("PRODUCT_UNAVAILABLE");
        problem.setDetail(getMessage());

        return problem;
    }
}
