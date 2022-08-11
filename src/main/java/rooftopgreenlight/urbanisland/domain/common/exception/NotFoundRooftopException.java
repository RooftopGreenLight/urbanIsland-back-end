package rooftopgreenlight.urbanisland.domain.common.exception;

public class NotFoundRooftopException extends RuntimeException{
    public NotFoundRooftopException() {
    }

    public NotFoundRooftopException(String message) {
        super(message);
    }

    public NotFoundRooftopException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundRooftopException(Throwable cause) {
        super(cause);
    }

    public NotFoundRooftopException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
