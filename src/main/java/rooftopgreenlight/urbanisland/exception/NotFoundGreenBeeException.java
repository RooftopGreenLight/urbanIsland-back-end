package rooftopgreenlight.urbanisland.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundGreenBeeException extends NotFoundException {

    public NotFoundGreenBeeException(String message) {
        super(message);
    }

}
