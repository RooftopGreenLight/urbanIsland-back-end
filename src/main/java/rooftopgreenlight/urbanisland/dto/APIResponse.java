package rooftopgreenlight.urbanisland.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import rooftopgreenlight.urbanisland.exception.ErrorCode;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class APIResponse<T> extends APIErrorResponse {
    private T data;

    protected APIResponse() {
        super(true, ErrorCode.CREATED.getCode(), ErrorCode.CREATED.getMessage());
        this.data = null;
    }

    protected APIResponse(T data) {
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> APIResponse<T> of(T data) {
        return new APIResponse(data);
    }

    public static <T> APIResponse<T> empty() {
        return new APIResponse(null);
    }

    public static <T> APIResponse<T> createEmpty() {
        return new APIResponse();
    }
}
