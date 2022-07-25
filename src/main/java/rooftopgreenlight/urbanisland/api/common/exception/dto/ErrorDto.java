package rooftopgreenlight.urbanisland.api.common.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDto {
    private String errorMessage;
    private List<ErrorDetailDto> errors = new ArrayList<>();
}
