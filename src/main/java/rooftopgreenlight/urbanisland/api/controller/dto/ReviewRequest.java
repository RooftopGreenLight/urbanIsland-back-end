package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class ReviewRequest {

    @Max(value = 5, message = "6 이상의 값을 입력 받을 수 없습니다.")
    @PositiveOrZero(message = "음수 값을 입력 받을 수 없습니다.")
    private int grade;
    private String content;
}
