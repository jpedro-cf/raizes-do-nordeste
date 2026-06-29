package raizes.nordeste.app.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class NotFoundException extends AppException{
    public NotFoundException(String message) {
        super(message);
    }
    @Override
    public ProblemDetail toProblemDetail(){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problem.setTitle("RESOURCE_NOT_FOUND");
        problem.setDetail(this.getMessage());

        return problem;
    }
}