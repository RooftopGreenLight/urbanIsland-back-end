package rooftopgreenlight.urbanisland.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class TokenDto {
    private String type;
    private String accessToken;
    private String refreshToken;
}
