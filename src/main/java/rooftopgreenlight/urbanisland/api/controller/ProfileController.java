package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.FileResponse;
import rooftopgreenlight.urbanisland.api.service.FileService;
import rooftopgreenlight.urbanisland.domain.file.service.ProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class ProfileController {

    private final FileService fileService;
    private final ProfileService profileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Member Profile 저장", notes = "정상 저장 시 저장 Url 반환")
    public APIResponse saveMemberProfile(@RequestParam("file") MultipartFile profile,
                                         @PK Long memberId) {
        return APIResponse.of(fileService.saveProfile(profile, memberId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Member Profile 조회", notes = "정상 조회 시 저장 Url 반환")
    public APIResponse getMemberProfile(@PK Long memberId) {
        return APIResponse.of(
                FileResponse.fromProfile(memberId, profileService.getProfileByMemberId(memberId))
        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Member Profile 삭제", notes = "삭제 성공 시 OK 반환")
    public APIResponse deleteMemberProfile(@PK Long memberId) {
        fileService.deleteProfile(memberId);

        return APIResponse.empty();
    }

}
