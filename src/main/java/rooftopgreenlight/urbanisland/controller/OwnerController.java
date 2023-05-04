package rooftopgreenlight.urbanisland.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.config.annotation.PK;
import rooftopgreenlight.urbanisland.dto.APIResponse;
import rooftopgreenlight.urbanisland.dto.greenbee.GreeningApplyPageResponse;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopResponse;
import rooftopgreenlight.urbanisland.service.OwnerService;
import rooftopgreenlight.urbanisland.service.RooftopGreeningApplyService;
import rooftopgreenlight.urbanisland.service.RooftopService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owners")
public class OwnerController {

    private final OwnerService ownerService;
    private final RooftopService rooftopService;
    private final RooftopGreeningApplyService greeningApplyService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "옥상지기 등록하기",
            notes = "요청 데이터(form-data) - key : confirmationFile(Multipart)")
    public APIResponse createOwner(@PK Long memberId,
                                   @RequestParam(name = "confirmationFile", required = false) MultipartFile file){

        ownerService.saveOwner(memberId, file);
        return APIResponse.createEmpty();
    }

    @GetMapping("/greenbee-waiting")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(14) 그린비 대기사항 - 옥상 정보 가져오기",
                notes = "요청 데이터 - 없음")
    public APIResponse getGreenBeeWaitingList(@PK Long memberId) {
        return APIResponse.of(RooftopResponse.getRooftopStatus(rooftopService.getNGGreenRooftopByMemberId(memberId)));
    }


    @GetMapping("/greenbee-waiting/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(14) 그린비 대기사항 - 옥상에 따른 그린비 목록 가져오기",
        notes = "요청 데이터(Parameter) - key -> page," +
                "(path) - key -> rooftopId")
    public APIResponse getGreenBeeWaitingList(@PathVariable(value = "rooftopId") Long rooftopId,
                                              @RequestParam("page") int page) {
        return APIResponse.of(GreeningApplyPageResponse.of(greeningApplyService.getGreenBeeWaitingList(page, rooftopId)));
    }

    @DeleteMapping("/delete-ngrooftop/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(14) 그린비 대기사항 - 공고 내리기",
        notes = "요청 데이터(path) - key -> rooftopId")
    public APIResponse deleteNGRooftop(@PK Long memberId,
                                       @PathVariable(value = "rooftopId") Long rooftopId) {
        rooftopService.deleteRooftop(memberId, rooftopId, true);
        return APIResponse.empty();
    }

    @GetMapping("/confirm-greenbee/{rooftopId}/{greenbeeId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(14) 그린비 대기사항 - 확정하기",
        notes = "요청 데이터(path) - key -> rooftopId, greenbeeId")
    public APIResponse confirmGreenBee(@PathVariable(value ="rooftopId") Long rooftopId,
                                       @PathVariable(value = "greenbeeId") Long greenbeeId,
                                       @PK Long memberId) {
        rooftopService.confirmGreenBeeNGRooftop(rooftopId, greenbeeId, memberId);
        return APIResponse.empty();
    }

    @GetMapping("/rooftop-status")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(13) 대기 옥상 진행사항",
        notes = "요청 데이터 - 없음")
    public APIResponse getRooftopStatus(@PK Long memberId) {
        return APIResponse.of(RooftopResponse.getRooftopStatus(rooftopService.getGreenRooftopByMemberId(memberId)));
    }
}
