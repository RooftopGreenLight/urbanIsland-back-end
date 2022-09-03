package rooftopgreenlight.urbanisland.domain.common.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundProfileException extends NotFoundException {

    public NotFoundProfileException(String message) {
        super(message);
    }
}
