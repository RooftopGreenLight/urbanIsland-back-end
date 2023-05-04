package rooftopgreenlight.urbanisland.exception;

public class ExpiredRefreshTokenException extends JwtException {

    public ExpiredRefreshTokenException(String message) {
        super(message);
    }

}
