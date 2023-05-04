package rooftopgreenlight.urbanisland.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundRooftopReviewException extends NotFoundException {

    public NotFoundRooftopReviewException(String message) {
        super(message);
    }

}
