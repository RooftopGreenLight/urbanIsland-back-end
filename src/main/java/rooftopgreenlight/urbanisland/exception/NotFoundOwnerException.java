package rooftopgreenlight.urbanisland.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundOwnerException extends NotFoundException {

    public NotFoundOwnerException(String message) {
        super(message);
    }

}
