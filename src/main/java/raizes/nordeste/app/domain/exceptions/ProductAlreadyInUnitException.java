package raizes.nordeste.app.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import raizes.nordeste.app.shared.exceptions.AppException;

public class ProductAlreadyInUnitException extends AppException {
    public ProductAlreadyInUnitException() {
        super("This product already exist in this unit.");
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("PRODUCT_ALREADY_IN_UNIT");
        problem.setDetail(getMessage());

        return problem;
    }
}
