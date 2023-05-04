package rooftopgreenlight.urbanisland.config.resolver;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "file")
public class FileDirProperties {

    private final String dir;

    public FileDirProperties(String dir) {
        this.dir = dir;
    }
}
