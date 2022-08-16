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

    @GetMapping("/greenbee-waiting")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(14) 그린비 대기사항 - 옥상, 그린비 목록 가져오기",
        notes = "요청 데이터(Parameter) - key : page")
    public APIResponse getGreenBeeWaitingList(@PK Long memberId,
                                              @RequestParam("page") int page) {
        return APIResponse.of(greeningApplyService.getGreenBeeWaitingList(page, memberId));
    }

    @DeleteMapping("/delete-ngrooftop/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(14) 그린비 대기사항 - 공고 내리기",
        notes = "요청 데이터(path) - /rooftopId")
    public APIResponse deleteNGRooftop(@PathVariable(value = "rooftopId") Long rooftopId) {
        rooftopService.deleteRooftop(rooftopId, true);
        return APIResponse.empty();
    }

    @GetMapping("/confirm-greenbee/{rooftopId}/{greenbeeId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(14) 그린비 대기사항 - 확정하기",
        notes = "요청 데이터(path) - /rooftopId/greenbeeId")
    public APIResponse confirmGreenBee(@PathVariable(value ="rooftopId") Long rooftopId,
                                      @PathVariable(value = "greenbeeId") Long greenbeeId) {
        rooftopService.confirmGreenBeeNGRooftop(rooftopId, greenbeeId);
        return APIResponse.empty();
    }
}
