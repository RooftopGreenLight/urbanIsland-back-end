package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.domain.owner.service.OwnerService;
import rooftopgreenlight.urbanisland.domain.rooftop.service.RooftopGreeningApplyService;
import rooftopgreenlight.urbanisland.domain.rooftop.service.RooftopService;

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

    /*******여기서부터 테스트 필요 ****/
    // (14) 그린비 대기사항
    // 옥상 목록과 신청한 그린비 목록 가져오기
    @GetMapping("/greenbee-waiting")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse getGreenBeeWaitingList(@PK Long memberId,
                                              @RequestParam("page") int page) {
        return APIResponse.of(greeningApplyService.getGreenBeeWaitingList(page, memberId));
    }

    // (14) 그린비 대기사항 - 공고 내리기 - 연쇄 삭제 확인 필요
    @DeleteMapping("/delete-ngrooftop/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse deleteNGRooftop(@PathVariable(value = "rooftopId") Long rooftopId) {
        rooftopService.deleteNGRooftop(rooftopId);
        return APIResponse.empty();
    }

    // (14) 확정하기
    // 누르면 해당 옥상의 해당 그린비를 제외한 나머지는 상태 변경하기
    @GetMapping("/confirm-greenbee/{rooftopId}/{greenbeeId}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse confirmGreenBee(@PathVariable(value ="rooftopId") Long rooftopId,
                                      @PathVariable(value = "greenbeeId") Long greenbeeId) {
        rooftopService.confirmGreenBeeNGRooftop(rooftopId, greenbeeId);
        return APIResponse.empty();
    }
}
