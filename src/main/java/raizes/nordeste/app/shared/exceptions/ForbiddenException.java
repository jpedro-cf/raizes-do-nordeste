package raizes.nordeste.app.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ForbiddenException extends AppException{
    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail(){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        problem.setTitle("Forbidden Access.");
        problem.setDetail(this.getMessage());

        return problem;
    }
}