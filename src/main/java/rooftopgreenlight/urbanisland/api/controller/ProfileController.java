package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.FileResponse;
import rooftopgreenlight.urbanisland.domain.file.service.FileService;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class ProfileController {

    private final FileService fileService;
    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Member Profile 저장",
            notes = "요청 데이터(Parameter) -> key : file, valueType : MultipartFile")
    public APIResponse saveMemberProfile(@RequestParam("file") MultipartFile profile,
                                         @PK Long memberId) {
        return APIResponse.of(FileResponse.fromProfile(fileService.saveProfile(profile, memberId)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Member Profile 조회",
            notes = "요청 데이터 -> 없음")
    public APIResponse getMemberProfile(@PK Long memberId) {
        return APIResponse.of(FileResponse.fromProfile(memberService.findByIdWithProfile(memberId).getProfile()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Member Profile 삭제",
            notes = "요청 데이터 -> JWT")
    public APIResponse deleteMemberProfile(@PK Long memberId) {
        fileService.deleteProfile(memberId);

        return APIResponse.empty();
    }

}
