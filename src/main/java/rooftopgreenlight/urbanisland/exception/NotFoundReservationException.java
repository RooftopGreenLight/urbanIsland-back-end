package rooftopgreenlight.urbanisland.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundReservationException extends NotFoundException {

    public NotFoundReservationException(String message) {
        super(message);
    }

}
