package backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {CustomRequestException.class})
    public ResponseEntity<?> handleCustomRequestException(RuntimeException exception) {
        CustomException customException = new CustomException(exception.getMessage(),
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<?> handleNullException(RuntimeException exception) {
        CustomException customException = new CustomException(exception.getMessage(),
                HttpStatus.resolve(404));
        return new ResponseEntity<>(customException, HttpStatus.resolve(404));
    }

    @ExceptionHandler(value = {AccessDeniedException.class, AuthenticationException.class})
    public ResponseEntity<?> handleAccessDeniedException(Exception exception) {
        CustomException customException = new CustomException("You do not have access to this page",
                HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(customException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleNotReadableException(RuntimeException exception) {
        CustomException customException = new CustomException("Your input is invalid",
                HttpStatus.resolve(400));
        return new ResponseEntity<>(customException, HttpStatus.resolve(400));
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    public ResponseEntity<?> handleInvalidException(InvalidRequestException exception) {
        CustomException customException = new CustomException(exception.getMessage(),
                HttpStatus.resolve(400));
        return new ResponseEntity<>(customException, HttpStatus.resolve(400));
    }


}
