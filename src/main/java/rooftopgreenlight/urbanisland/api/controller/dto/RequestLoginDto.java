package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RequestLoginDto {
    @NotBlank(message = "올바른 이메일 입력 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "올바른 이메일 입력 형식이 아닙니다.")
    private String password;
}
