package rooftopgreenlight.urbanisland.domain.exception;

public class NotFoundProfileException extends RuntimeException{

    public NotFoundProfileException() {
        super();
    }

    public NotFoundProfileException(String message) {
        super(message);
    }

    public NotFoundProfileException(String message, Throwable cause) {
        super(message, cause);
    }
}
