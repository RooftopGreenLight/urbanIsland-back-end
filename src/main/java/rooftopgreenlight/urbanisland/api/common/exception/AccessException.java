package rooftopgreenlight.urbanisland.api.common.exception;

import rooftopgreenlight.urbanisland.domain.common.exception.ClientException;

public class AccessException extends ClientException {
    public AccessException(String message) {
        super(message);
    }
}
