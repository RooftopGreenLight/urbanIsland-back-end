package rooftopgreenlight.urbanisland.exception;

public class ClientException extends IllegalArgumentException {
    public ClientException(String s) {
        super(s);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
