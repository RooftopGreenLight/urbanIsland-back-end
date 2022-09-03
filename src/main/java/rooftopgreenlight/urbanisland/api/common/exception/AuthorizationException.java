package rooftopgreenlight.urbanisland.api.common.exception;

public class AuthorizationException extends JwtException {
    public AuthorizationException(String message) {
        super(message);
    }

}
