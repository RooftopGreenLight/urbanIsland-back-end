package rooftopgreenlight.urbanisland.domain.exception;

public class NotFoundMemberException extends RuntimeException{

    public NotFoundMemberException() {
        super();
    }

    public NotFoundMemberException(String message) {
        super(message);
    }

    public NotFoundMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
