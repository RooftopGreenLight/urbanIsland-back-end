package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ResponseDto<T> {
    private T data;
}
