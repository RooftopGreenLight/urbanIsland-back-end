package rooftopgreenlight.urbanisland.api.common.exception;

public class DuplicatedMemberException extends RuntimeException {
    public DuplicatedMemberException() {
    }

    public DuplicatedMemberException(String message) {
        super(message);
    }

    public DuplicatedMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedMemberException(Throwable cause) {
        super(cause);
    }

    public DuplicatedMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
