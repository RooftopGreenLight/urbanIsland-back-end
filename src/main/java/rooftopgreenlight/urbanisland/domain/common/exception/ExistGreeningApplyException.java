package rooftopgreenlight.urbanisland.domain.common.exception;

public class ExistGreeningApplyException extends RuntimeException{
    public ExistGreeningApplyException() {
    }

    public ExistGreeningApplyException(String message) {
        super(message);
    }

    public ExistGreeningApplyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistGreeningApplyException(Throwable cause) {
        super(cause);
    }

    public ExistGreeningApplyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
