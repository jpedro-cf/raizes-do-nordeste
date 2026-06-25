package raizes.nordeste.app.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import raizes.nordeste.app.shared.exceptions.AppException;

public class EmailInUseException extends AppException {
    public EmailInUseException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("E-mail already in use.");
        problem.setDetail(getMessage());

        return problem;
    }
}
