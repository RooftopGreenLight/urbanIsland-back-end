package rooftopgreenlight.urbanisland.exception;

public class AuthorizationException extends JwtException {
    public AuthorizationException(String message) {
        super(message);
    }

}
