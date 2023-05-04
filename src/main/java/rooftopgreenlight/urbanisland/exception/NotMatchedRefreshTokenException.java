package rooftopgreenlight.urbanisland.exception;

public class NotMatchedRefreshTokenException extends JwtException {
    public NotMatchedRefreshTokenException(String message) {
        super(message);
    }

}
