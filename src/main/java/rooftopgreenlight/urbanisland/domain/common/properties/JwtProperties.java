package rooftopgreenlight.urbanisland.domain.common.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String type;
    private String expirationTime;
    private String refreshExpirationTime;
    private String secret;

    public JwtProperties(String type, String expirationTime, String refreshExpirationTime, String secret) {
        this.type = type;
        this.expirationTime = expirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        this.secret = secret;
    }
}
