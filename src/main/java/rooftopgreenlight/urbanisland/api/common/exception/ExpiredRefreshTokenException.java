package rooftopgreenlight.urbanisland.api.common.exception;

public class ExpiredRefreshTokenException extends RuntimeException {
    public ExpiredRefreshTokenException() {
    }

    public ExpiredRefreshTokenException(String message) {
        super(message);
    }

    public ExpiredRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredRefreshTokenException(Throwable cause) {
        super(cause);
    }

    public ExpiredRefreshTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
