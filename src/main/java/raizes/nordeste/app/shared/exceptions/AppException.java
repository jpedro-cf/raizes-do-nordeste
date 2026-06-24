package raizes.nordeste.app.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class AppException extends RuntimeException{
    public AppException(String message){
        super(message);
    }
    public ProblemDetail toProblemDetail(){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problem.setTitle("Internal Server Error.");

        return problem;
    }
}