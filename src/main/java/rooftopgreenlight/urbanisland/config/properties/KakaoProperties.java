package rooftopgreenlight.urbanisland.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "oauth2.kakao")
public class KakaoProperties {

    private final String getTokenUri;
    private final String userInfoUri;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUrl;

    public KakaoProperties(String getTokenUri, String userInfoUri, String clientId, String clientSecret, String redirectUrl) {
        this.getTokenUri = getTokenUri;
        this.userInfoUri = userInfoUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
    }
}
