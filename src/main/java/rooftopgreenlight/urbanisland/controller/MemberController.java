package rooftopgreenlight.urbanisland.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.config.annotation.PK;
import rooftopgreenlight.urbanisland.domain.member.Member;
import rooftopgreenlight.urbanisland.service.MemberService;
import rooftopgreenlight.urbanisland.dto.APIResponse;
import rooftopgreenlight.urbanisland.dto.member.MemberResponse;
import rooftopgreenlight.urbanisland.dto.member.NicknameRequest;
import rooftopgreenlight.urbanisland.dto.member.PasswordRequest;
import rooftopgreenlight.urbanisland.dto.member.PhoneNumberRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "회원 정보 가져오기",
            notes = "요청 데이터 - 없음")
    public APIResponse getMemberInfo(@PK Long memberId) {
        Member findMember = memberService.findByIdWithProfile(memberId);
        return APIResponse.of(MemberResponse.of(findMember));
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그인 후 회원 비밀번호 변경",
            notes = "요청 데이터(Body) - key : password, valueType : String")
    public APIResponse changeMemberPassword(@PK Long memberId,
                                            @RequestBody @Validated PasswordRequest request) {
        memberService.changePassword(memberId, request.getPassword());
        return APIResponse.empty();
    }

    @PostMapping("/change-phone-number")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그인 후 회원 전화번호 변경",
            notes = "요청 데이터(Body) - key : phoneNumber, value : String(xxx-xxxx-xxxx)")
    public APIResponse changeMemberPhoneNumber(@PK Long memberId,
                                               @RequestBody @Validated PhoneNumberRequest request) {
        memberService.changePhoneNumber(memberId, request.getPhoneNumber());

        return APIResponse.empty();
    }

    @PostMapping("/change-nickname")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그인 후 회원 전화번호 변경",
            notes = "요청 데이터(Body) - key : nickname")
    public APIResponse changeMemberNickname(@PK Long memberId,
                                            @RequestBody @Validated NicknameRequest request) {
        memberService.changeNickname(memberId, request.getNickname());

        return APIResponse.empty();
    }
}
