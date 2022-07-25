package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RequestJoinDto {
    @Email(message = "올바른 이메일 입력 형식이 아닙니다.")
    @NotBlank(message = "올바른 이메일 입력 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "올바른 비밀번호 입력 형식이 아닙니다.")
    private String password;
    @NotBlank(message = "올바른 이름 입력 형식이 아닙니다.")
    private String name;
    @NotBlank(message = "올바른 전화번호 입력 형식이 아닙니다.")
    private String phoneNumber;
}
