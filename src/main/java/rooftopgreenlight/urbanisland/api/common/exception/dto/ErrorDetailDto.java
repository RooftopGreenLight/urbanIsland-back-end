package rooftopgreenlight.urbanisland.api.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetailDto {
    private String field;
    private String errorMessage;
}
