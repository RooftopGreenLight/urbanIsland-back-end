package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.MemberResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.PasswordRequest;
import rooftopgreenlight.urbanisland.api.controller.dto.PhoneNumberRequest;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;

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
}
