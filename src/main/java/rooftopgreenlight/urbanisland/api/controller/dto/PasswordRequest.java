package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PasswordRequest {

    private String email;

    @NotBlank(message = "올바른 비밀번호 입력 형식이 아닙니다.")
    private String password;

}
