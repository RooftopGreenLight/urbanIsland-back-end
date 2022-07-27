package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import rooftopgreenlight.urbanisland.api.common.exception.error.ErrorCode;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class APIResponse<T> extends APIErrorResponse {
    private T data;

    protected APIResponse(T data) {
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> APIResponse<T> of(T data) {
        return new APIResponse(data);
    }

    public static <T> APIResponse<T> empty(T data) {
        return new APIResponse(null);
    }
}
