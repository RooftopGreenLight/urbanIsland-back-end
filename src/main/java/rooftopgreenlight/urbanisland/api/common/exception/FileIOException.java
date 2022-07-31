package rooftopgreenlight.urbanisland.api.common.exception;

public class FileIOException extends RuntimeException{
    public FileIOException() {
    }

    public FileIOException(String message) {
        super(message);
    }

    public FileIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
