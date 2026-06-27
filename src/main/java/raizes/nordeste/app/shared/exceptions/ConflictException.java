package raizes.nordeste.app.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ConflictException extends AppException{
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("CONFLICT_EXCEPTION");
        problem.setDetail(this.getMessage());

        return problem;
    }
}
