package rooftopgreenlight.urbanisland.api.common.exception;

public class ExpiredRefreshTokenException extends JwtException {

    public ExpiredRefreshTokenException(String message) {
        super(message);
    }

}
