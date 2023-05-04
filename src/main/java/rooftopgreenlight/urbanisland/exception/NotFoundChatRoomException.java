package rooftopgreenlight.urbanisland.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundChatRoomException extends NotFoundException {
    public NotFoundChatRoomException(String message) {
        super(message);
    }

}
