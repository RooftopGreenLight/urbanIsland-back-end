package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "서버 정상 동작하는지 확인", notes = "정상 동작 시 '서버 동작 중' 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 동작"),
            @ApiResponse(code = 500, message = "서버 문제")
    })
    public APIResponse health() {
        return APIResponse.of("서버 동작 중...");
    }

    /**
     * 로그인
     * @param loginDto
     * @return JWT
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그인 기능", notes = "정상 동작 시 로그인 성공")
    public APIResponse login(@Validated @RequestBody LoginRequest loginDto) {
        return APIResponse.of(authService.login(loginDto.getEmail(), loginDto.getPassword()));
    }

    /**
     * 회원가입
     * @param joinDto
     * @return 회원가입 성공 여부
     */
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "회원가입 기능", notes = "정상 동작 시 회원가입 성공" )
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
    @ApiOperation(value = "회원 중복 체크 기능", notes = "정상 동작 시 회원 중복 체크 성공")
    public APIResponse checkEmail(@RequestParam(value = "email") String email) {
        if(memberService.existByEmail(email)) {
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
    @ApiOperation(value = "이메일 인증 기능", notes = "정상 동작 시 이메일 전송 및 인증 성공")
    public APIResponse verifyEmail(@RequestParam(value = "email") String email) {
        return APIResponse.of(authService.send(email));
    }

    /**
     * Access-token 갱신 기능
     * @param refreshToken
     * @return 새로운 access-token
     */
    @GetMapping("/check-refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Access-Token 갱신 기능", notes = "정상 동작 시 Access-Token 갱신 성공")
    public APIResponse checkRefreshToken(@RequestHeader(value = "refresh-token") String refreshToken) {
        return APIResponse.of(authService.checkRefreshToken(refreshToken));
    }

    private Member createMember(JoinRequest joinDto) {
        return Member.createMember()
                .email(joinDto.getEmail())
                .password(encoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .authority(Authority.ROLE_USER)
                .build();
    }

}
