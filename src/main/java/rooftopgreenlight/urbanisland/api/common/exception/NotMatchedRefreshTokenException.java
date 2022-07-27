package rooftopgreenlight.urbanisland.api.common.exception;

public class NotMatchedRefreshTokenException extends RuntimeException {
    public NotMatchedRefreshTokenException() {
    }

    public NotMatchedRefreshTokenException(String message) {
        super(message);
    }

    public NotMatchedRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchedRefreshTokenException(Throwable cause) {
        super(cause);
    }

    public NotMatchedRefreshTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
