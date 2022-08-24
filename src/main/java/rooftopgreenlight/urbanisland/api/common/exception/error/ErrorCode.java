package rooftopgreenlight.urbanisland.api.common.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(HttpStatus.OK.value(), HttpStatus.OK, "OK!"),
    CREATED(HttpStatus.CREATED.value(), HttpStatus.CREATED, "CREATED!"),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, "FORBIDDEN!"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Bad Request"),
    SPRING_BAD_REQUEST(HttpStatus.BAD_REQUEST.value() + 51, HttpStatus.BAD_REQUEST, "Spring-detected bad request"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST.value() + 52, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "Requested Resource Is Not Found"),
    JWT_ACCESS_ERROR(HttpStatus.BAD_REQUEST.value() + 60, HttpStatus.BAD_REQUEST, "Please give refresh-token."),
    JWT_REFRESH_ERROR(HttpStatus.BAD_REQUEST.value() + 61, HttpStatus.BAD_REQUEST, "Refresh-Token Error"),
    MAIL_SEND_ERROR(HttpStatus.BAD_REQUEST.value() + 62, HttpStatus.BAD_REQUEST, "Mail-Send Error"),
    ACCESS_ERROR(HttpStatus.BAD_REQUEST.value() + 63, HttpStatus.BAD_REQUEST, "Unusable token."),


    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error"),
    SPRING_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value() + 51, HttpStatus.INTERNAL_SERVER_ERROR, "Spring-Detected Internal Error"),
    DATA_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value() + 52, HttpStatus.INTERNAL_SERVER_ERROR, "Data Access Error"),
    FILE_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value() + 53, HttpStatus.INTERNAL_SERVER_ERROR, "File I/O Error");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
