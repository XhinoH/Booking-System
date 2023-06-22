package backend.exception;

public class CustomRequestException extends RuntimeException{

    public CustomRequestException(String message) {
        super(message);
    }

    public CustomRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
