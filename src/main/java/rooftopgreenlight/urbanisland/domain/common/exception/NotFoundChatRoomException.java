package rooftopgreenlight.urbanisland.domain.common.exception;

public class NotFoundChatRoomException extends RuntimeException {
    public NotFoundChatRoomException() {
    }

    public NotFoundChatRoomException(String message) {
        super(message);
    }

    public NotFoundChatRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundChatRoomException(Throwable cause) {
        super(cause);
    }

    public NotFoundChatRoomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
