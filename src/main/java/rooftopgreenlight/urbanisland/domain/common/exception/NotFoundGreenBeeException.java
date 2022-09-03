package rooftopgreenlight.urbanisland.domain.common.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundGreenBeeException extends NotFoundException {

    public NotFoundGreenBeeException(String message) {
        super(message);
    }

}
