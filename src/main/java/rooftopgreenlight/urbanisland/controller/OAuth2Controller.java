package rooftopgreenlight.urbanisland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rooftopgreenlight.urbanisland.dto.APIResponse;
import rooftopgreenlight.urbanisland.service.Oauth2Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class OAuth2Controller {

    private final Oauth2Service oauth2Service;

    @GetMapping("/login/kakao")
    public APIResponse oauth2KakaoLogin(@RequestParam("code") String code) {
        return APIResponse.of(oauth2Service.loginKakao(code));
    }

    @GetMapping("/login/google")
    public APIResponse oauth2GoogleLogin(@RequestParam("code") String code) {
        return APIResponse.of(oauth2Service.loginGoogle(code));
    }

    @GetMapping("/login/naver")
    public APIResponse oauth2NaverLogin(@RequestParam("code") String code) {
        return APIResponse.of(oauth2Service.loginNaver(code));
    }

}
