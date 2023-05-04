package rooftopgreenlight.urbanisland.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundRooftopException extends NotFoundException {

    public NotFoundRooftopException(String message) {
        super(message);
    }

}
