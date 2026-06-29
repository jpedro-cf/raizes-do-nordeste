package raizes.nordeste.app.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class AppException extends RuntimeException{
    public AppException(String message){
        super(message);
    }
    public ProblemDetail toProblemDetail(){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problem.setTitle("INTERNAL_SERVER_ERROR");

        return problem;
    }
}