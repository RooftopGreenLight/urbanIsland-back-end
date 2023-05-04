package rooftopgreenlight.urbanisland.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import rooftopgreenlight.urbanisland.config.resolver.JwtResolver;
import rooftopgreenlight.urbanisland.config.resolver.PKResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtResolver jwtResolver;
    private final PKResolver pkResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtResolver);
        resolvers.add(pkResolver);
    }
}
