package raizes.nordeste.app.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import raizes.nordeste.app.api.exceptions.AppException;

public class EmailInUseException extends AppException {
    public EmailInUseException() {
        super("The email provided is already in use.");
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("EMAIL_IN_USE");
        problem.setDetail(getMessage());

        return problem;
    }
}
