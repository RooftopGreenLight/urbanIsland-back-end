package rooftopgreenlight.urbanisland.api.common.exception;

import rooftopgreenlight.urbanisland.domain.common.exception.ClientException;

public class DuplicatedMemberException extends ClientException {
    public DuplicatedMemberException(String message) {
        super(message);
    }

}
