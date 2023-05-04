package rooftopgreenlight.urbanisland.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import rooftopgreenlight.urbanisland.exception.ErrorCode;

@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class APIErrorResponse<T> {

    private final boolean success;
    private final Integer errorCode;
    private final T message;

    public static <T> APIErrorResponse<T> of(boolean success, Integer errorCode, T message) {
        return new APIErrorResponse(success, errorCode, message);
    }

    public static <T> APIErrorResponse<T> of(Boolean success, ErrorCode errorCode) {
        return new APIErrorResponse(success, errorCode.getCode(), errorCode.getMessage());
    }

    public static APIErrorResponse of(Boolean success, ErrorCode errorCode, Exception e) {
        return new APIErrorResponse(success, errorCode.getCode(), errorCode.getMessage(e));
    }
}
