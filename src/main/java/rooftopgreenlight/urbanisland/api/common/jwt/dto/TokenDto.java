package rooftopgreenlight.urbanisland.api.common.jwt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenDto {
    private String type;
    private String accessToken;
    private String refreshToken;

    @Builder(builderMethodName = "createTokenDto")
    public TokenDto(String type, String accessToken, String refreshToken) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
