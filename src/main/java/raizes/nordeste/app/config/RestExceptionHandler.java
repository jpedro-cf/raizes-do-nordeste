package raizes.nordeste.app.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import raizes.nordeste.app.shared.exceptions.AppException;

import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ProblemDetail handleApplicationException(AppException e){
        return e.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var errors = e.getFieldErrors()
                .stream()
                .map(f -> Map.of(
                        "field", f.getField(),
                        "message", f.getDefaultMessage() != null ? f.getDefaultMessage() : "invalid field"
                ))
                .toList();

        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("INVALID_PARAMS");
        pb.setProperty("detail", errors);

        return pb;
    }
}