package rooftopgreenlight.urbanisland.api.common.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
    private String username;

    public MailProperties(String username) {
        this.username = username;
    }
}
