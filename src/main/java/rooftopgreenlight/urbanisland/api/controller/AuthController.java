package rooftopgreenlight.urbanisland.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.controller.dto.RequestLoginDto;
import rooftopgreenlight.urbanisland.api.controller.dto.ResponseDto;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Value("${jasypt.encryptor.password}")
    private String password;

    @GetMapping("/health")
    public String health() {
        return password;
//        return "Client Access Successful!";
    }

    @PostMapping("/login")
    public ResponseDto login(@Validated @RequestBody RequestLoginDto loginDto) {
        return null;
    }

}
