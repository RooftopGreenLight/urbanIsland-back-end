package rooftopgreenlight.urbanisland.domain.common.exception;

import com.amazonaws.services.kms.model.NotFoundException;

public class NotFoundMemberException extends NotFoundException {

    public NotFoundMemberException(String message) {
        super(message);
    }

}
