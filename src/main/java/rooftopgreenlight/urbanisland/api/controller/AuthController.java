package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.common.jwt.dto.TokenDto;
import rooftopgreenlight.urbanisland.api.controller.dto.RequestJoinDto;
import rooftopgreenlight.urbanisland.api.controller.dto.RequestLoginDto;
import rooftopgreenlight.urbanisland.api.controller.dto.ResponseDto;
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
    public ResponseDto health() {
        return new ResponseDto("서버 동작 중...");
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그인 기능", notes = "정상 동작 시 로그인 성공")
    public ResponseDto login(@Validated @RequestBody RequestLoginDto loginDto) {
        return new ResponseDto(authService.login(loginDto.getEmail(), loginDto.getPassword()));
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "회원가입 기능", notes = "정상 동작 시 회원가입 성공" )
    public ResponseDto join(@Validated @RequestBody RequestJoinDto joinDto) {
        memberService.save(createMember(joinDto));
        
        return new ResponseDto("회원가입 성공!");
    }

    private Member createMember(RequestJoinDto joinDto) {
        return Member.createMember()
                .email(joinDto.getEmail())
                .password(encoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .phoneNumber(joinDto.getPhoneNumber())
                .authority(Authority.ROLE_USER)
                .build();
    }

}
