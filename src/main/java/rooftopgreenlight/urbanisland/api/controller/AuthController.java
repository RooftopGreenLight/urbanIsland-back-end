package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.common.annotation.Jwt;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.common.exception.DuplicatedMemberException;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.JoinRequest;
import rooftopgreenlight.urbanisland.api.controller.dto.LoginRequest;
import rooftopgreenlight.urbanisland.api.service.AuthService;
import rooftopgreenlight.urbanisland.domain.member.entity.Authority;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;

    /**
     * 로그인
     * @param loginDto
     * @return JWT
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그인 기능", notes = "key - email(@ 이메일 형식, null, blank X) , password(null, blank X)")
    public APIResponse login(@Validated @RequestBody LoginRequest loginDto) {
        return APIResponse.of(authService.login(loginDto.getEmail(), loginDto.getPassword()));
    }

    /**
     * 로그아웃
     */
    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그아웃 기능", notes = "요청 데이터 X")
    public APIResponse logout(@PK Long memberId,
                              @Jwt String token) {
        authService.logout(memberId, token);

        return APIResponse.empty();
    }

    /**
     * 회원가입
     * @param joinDto
     * @return 회원가입 성공 여부
     */
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "회원가입 기능", notes = "key - email(@ 이메일 형식, null, blank X) , password(null, blank X), nickname(null, blank X)" )
    public APIResponse join(@Validated @RequestBody JoinRequest joinDto) {
        memberService.save(createMember(joinDto));
        return APIResponse.of("회원가입 성공!");
    }

    /**
     * 이메일 중복 확인
     * @param email
     * @return 중복 체크 성공 여부
     */
    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "회원 중복 체크 기능", notes = "key - email(@ 이메일 형식, null, blank X)")
    public APIResponse checkEmail(@RequestParam(value = "email") String email) {
        if(memberService.existByEmail(email)) {
            throw new DuplicatedMemberException("이미 존재하는 회원입니다.");
        }
        return APIResponse.of("성공!");
    }

    /**
     * 닉네임 중복 확인
     * @param nickname
     * @return 중복 체크 성공 여부
     */
    @GetMapping("/check-nickname")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "회원 닉네임 중복 체크 기능", notes = "key - nickname(blank X)")
    public APIResponse checkNickname(@RequestParam(value = "nickname") String nickname) {
        if(memberService.existByNickname(nickname)) {
            throw new DuplicatedMemberException("이미 존재하는 회원입니다.");
        }
        return APIResponse.of("성공!");
    }

    /**
     * 이메일 인증
     * @param email
     * @return 이메일 인증번호
     */
    @GetMapping("/verify-email")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "이메일 인증 기능", notes = "key - email(@ 이메일 형식, null, blank X)")
    public APIResponse verifyEmail(@RequestParam(value = "email") String email) {
        return APIResponse.of(authService.send(email));
    }

    /**
     * Access-token 갱신 기능
     * @param refreshToken
     * @return 새로운 access-token
     */
    @GetMapping("/{memberId}/check-refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Access-Token 갱신 기능", notes = "header - refresh-token, key(path) - memberId")
    public APIResponse checkRefreshToken(@PathVariable("memberId") Long memberId,
                                         @RequestHeader(value = "refresh-token") String refreshToken) {
        return APIResponse.of(authService.checkRefreshToken(refreshToken, memberId));
    }

    private Member createMember(JoinRequest joinDto) {
        return Member.createMember()
                .email(joinDto.getEmail())
                .password(encoder.encode(joinDto.getPassword()))
                .nickname(joinDto.getNickname())
                .authority(Authority.ROLE_USER)
                .build();
    }

}
