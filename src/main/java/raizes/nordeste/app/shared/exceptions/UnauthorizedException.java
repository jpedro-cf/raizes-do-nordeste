package raizes.nordeste.app.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UnauthorizedException extends AppException{
    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail(){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problem.setTitle("UNAUTHORIZED");
        problem.setDetail(this.getMessage());

        return problem;
    }
}