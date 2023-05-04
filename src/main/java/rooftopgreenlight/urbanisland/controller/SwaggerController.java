package rooftopgreenlight.urbanisland.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {

    @GetMapping("/api-docs")
    public String apiDocs() {
        return "redirect:/swagger-ui/";
    }
}
