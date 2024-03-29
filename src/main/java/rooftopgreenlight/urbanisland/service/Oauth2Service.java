package rooftopgreenlight.urbanisland.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rooftopgreenlight.urbanisland.security.JwtProvider;
import rooftopgreenlight.urbanisland.dto.member.TokenDto;
import rooftopgreenlight.urbanisland.config.properties.GoogleProperties;
import rooftopgreenlight.urbanisland.config.properties.KakaoProperties;
import rooftopgreenlight.urbanisland.config.properties.NaverProperties;
import rooftopgreenlight.urbanisland.dto.oauth.GoogleToken;
import rooftopgreenlight.urbanisland.dto.oauth.GoogleUserInfo;
import rooftopgreenlight.urbanisland.dto.oauth.KakaoToken;
import rooftopgreenlight.urbanisland.dto.oauth.KakaoUserInfo;
import rooftopgreenlight.urbanisland.dto.member.MemberResponse;
import rooftopgreenlight.urbanisland.domain.file.Profile;
import rooftopgreenlight.urbanisland.domain.member.Authority;
import rooftopgreenlight.urbanisland.domain.member.Member;
import rooftopgreenlight.urbanisland.dto.oauth.NaverToken;
import rooftopgreenlight.urbanisland.dto.oauth.NaverUserInfo;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Oauth2Service {

    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;
    private final MemberService memberService;
    private final KakaoProperties kakaoProperties;
    private final NaverProperties naverProperties;
    private final GoogleProperties googleProperties;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse loginKakao(String code) {
        KakaoToken kakaoToken = getKakaoToken(code);
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo("Bearer " + kakaoToken.getAccessToken());

        char[] idChars = getId(kakaoUserInfo.getId().toString());

        Member member = getMember(String.valueOf(idChars) + "@kakao.com", kakaoProperties.getClientSecret(),
                getTempNickname(), kakaoUserInfo.getProperties().getProfileImage());

        TokenDto tokenDto = getTokenDto(member);

        return MemberResponse.of(member.getId(), tokenDto);
    }

    @Transactional
    public MemberResponse loginNaver(String code) {
        NaverToken naverToken = getNaverToken(code);
        NaverUserInfo naverUserInfo = getNaverUserInfo(naverToken);

        char[] idChars = getId(naverUserInfo.getId());

        Member member = getMember(String.valueOf(idChars) + "@naver.com",
                naverProperties.getClientSecret(), getTempNickname(), null);

        TokenDto tokenDto = getTokenDto(member);

        return MemberResponse.of(member.getId(), tokenDto);
    }

    @Transactional
    public MemberResponse loginGoogle(String code) {
        GoogleToken googleToken = getGoogleToken(code);

        GoogleUserInfo googleUserInfo = getGoogleUserInfo(googleToken);

        char[] idChars = getId(googleUserInfo.getId());

        Member member = getMember(String.valueOf(idChars) + "@google.com",
                googleProperties.getClientSecret(), getTempNickname(), null);

        TokenDto tokenDto = getTokenDto(member);

        return MemberResponse.of(member.getId(), tokenDto);
    }

    private GoogleUserInfo getGoogleUserInfo(GoogleToken googleToken) {
        String url = googleProperties.getUserInfoUri() + "?access_token=" + googleToken.getAccessToken();
        return restTemplate.getForEntity(url, GoogleUserInfo.class).getBody();
    }

    private GoogleToken getGoogleToken(String code) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("client_id", googleProperties.getClientId());
        data.add("client_secret", googleProperties.getClientSecret());
        data.add("redirect_uri", googleProperties.getRedirectUrl());
        data.add("grant_type", "authorization_code");
        data.add("code", code);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        return restTemplate
                .exchange(createPostRequestEntity(googleProperties.getGetTokenUri(), httpHeaders, data), GoogleToken.class)
                .getBody();
    }

    private NaverUserInfo getNaverUserInfo(NaverToken naverToken) {
        String token = "Bearer " + naverToken.getAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", token);

        ResponseEntity<String> result = restTemplate.exchange(createGetRequestEntity(naverProperties.getUserInfoUri(), httpHeaders), String.class);
        Gson gson = new Gson();
        JsonElement response = gson.fromJson(result.getBody(), JsonObject.class).get("response");
        NaverUserInfo naverUserInfo = gson.fromJson(response, NaverUserInfo.class);

        return naverUserInfo;
    }

    private NaverToken getNaverToken(String code) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "authorization_code");
        data.add("client_id", naverProperties.getClientId());
        data.add("client_secret", naverProperties.getClientSecret());
        data.add("code", code);

        return restTemplate
                .exchange(createPostRequestEntity(naverProperties.getGetTokenUri(), null, data), NaverToken.class)
                .getBody();
    }

    private KakaoToken getKakaoToken(String code) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "authorization_code");
        data.add("client_id", kakaoProperties.getClientId());
        data.add("redirect_uri", kakaoProperties.getRedirectUrl());
        data.add("code", code);
        data.add("client_secret", kakaoProperties.getClientSecret());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return restTemplate.exchange(
                        createPostRequestEntity(kakaoProperties.getGetTokenUri(), httpHeaders, data),
                KakaoToken.class
        ).getBody();
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", accessToken);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        ResponseEntity<KakaoUserInfo> result =
                restTemplate.exchange(createPostRequestEntity(kakaoProperties.getUserInfoUri(), httpHeaders, null), KakaoUserInfo.class);
        return result.getBody();
    }

    private static char[] getId(String kakaoUserInfo) {
        char[] idChars = kakaoUserInfo.toCharArray();
        for (int i = 3; i < idChars.length; i++) {
            idChars[i] = '*';
        }
        return idChars;
    }

    private RequestEntity createPostRequestEntity(String url, HttpHeaders httpHeaders, MultiValueMap map) {
        return RequestEntity.post(url).headers(httpHeaders).body(map);
    }

    private RequestEntity createGetRequestEntity(String url, HttpHeaders httpHeaders) {
        return RequestEntity.get(url).headers(httpHeaders).build();
    }

    private Member getMember(String email, String password, String nickname, String profileUrl) {
        if (memberService.existByEmail(email)) {
            return memberService.findByEmail(email);
        }

        Member member = Member.createMember()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER).build();

        if (profileUrl != null) saveProfile(profileUrl, member);

        return memberService.save(member);
    }

    private Profile createProfile(String profileUrl, String ext) {
        return Profile.createProfile()
                .fileUrl(profileUrl)
                .uploadFilename(profileUrl)
                .storeFilename(profileUrl)
                .type(ext)
                .build();
    }

    private void saveProfile(String profileUrl, Member member) {
        String ext = getExt(profileUrl);

        Profile profile = createProfile(profileUrl, ext);
        member.changeProfile(profile);
    }

    private TokenDto getTokenDto(Member member) {
        return jwtProvider
                .createJwt(String.valueOf(member.getId()), member.getEmail(), member.getAuthority().toString(), null);
    }

    private String getExt(String profileUrl) {
        String[] splits = profileUrl.split("\\.");
        int len = splits.length - 1;
        String ext = splits[len];
        return ext;
    }

    private String getTempNickname() {
        return UUID.randomUUID().toString().substring(0, 13);
    }
}
