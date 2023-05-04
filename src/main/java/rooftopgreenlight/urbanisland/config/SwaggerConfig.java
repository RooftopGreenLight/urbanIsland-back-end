package rooftopgreenlight.urbanisland.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Set;

@EnableWebMvc
@Configuration
public class SwaggerConfig {

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("Urban-Island API")
                .description("Urban-Island API Docs").build();
    }

    @Bean
    public Docket swaggerAPI() {
        return new Docket(DocumentationType.OAS_30)
                .consumes(Set.of("application/json;charset=UTF-8", "application/x-www-form-urlencoded"))
                .produces(Set.of("application/json;charset=UTF-8"))
                .apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("rooftopgreenlight.urbanisland.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

}
