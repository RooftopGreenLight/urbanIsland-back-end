package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class GreenBeeRequest {

    @NotBlank(message = "올바른 사무실 전화번호 입력 형식이 아닙니다.")
    private String officeNumber;
    @NotBlank(message = "올바른 멘트 입력 형식이 아닙니다.")
    private String content;
    @NotBlank(message = "올바른 도/광역시 입력 형식이 아닙니다.")
    private String city;
    @NotBlank(message = "올바른 시/군/구 입력 형식이 아닙니다.")
    private String district;
    @NotBlank(message = "올바른 세부 주소 입력 형식이 아닙니다.")
    private String detail;

}
