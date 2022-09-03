package rooftopgreenlight.urbanisland.api.common.exception;

public class NotMatchedRefreshTokenException extends JwtException {
    public NotMatchedRefreshTokenException(String message) {
        super(message);
    }

}
