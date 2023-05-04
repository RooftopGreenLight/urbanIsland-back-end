package rooftopgreenlight.urbanisland.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class KakaoUserInfo {

    private Long id;
    private Properties properties;

    @Getter
    @NoArgsConstructor
    public class Properties {
        @JsonProperty("profile_image")
        private String profileImage;
        private String nickname;
    }
}
