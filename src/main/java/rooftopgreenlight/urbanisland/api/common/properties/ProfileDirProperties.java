package rooftopgreenlight.urbanisland.api.common.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "dir")
public class ProfileDirProperties {

    private final String profileDir;

    public ProfileDirProperties(String profileDir) {
        this.profileDir = profileDir;
    }
}
