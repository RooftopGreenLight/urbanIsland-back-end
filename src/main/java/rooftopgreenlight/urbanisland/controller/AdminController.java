package rooftopgreenlight.urbanisland.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.domain.model.Progress;
import rooftopgreenlight.urbanisland.service.GreenBeeService;
import rooftopgreenlight.urbanisland.dto.greenbee.GreenBeeDto;
import rooftopgreenlight.urbanisland.service.OwnerService;
import rooftopgreenlight.urbanisland.dto.owner.OwnerDto;
import rooftopgreenlight.urbanisland.service.RooftopService;
import rooftopgreenlight.urbanisland.dto.APIResponse;
import rooftopgreenlight.urbanisland.dto.greenbee.GreenBeeInfoResponse;
import rooftopgreenlight.urbanisland.dto.greenbee.GreenBeePageResponse;
import rooftopgreenlight.urbanisland.dto.owner.OwnerInfoResponse;
import rooftopgreenlight.urbanisland.dto.owner.OwnerPageResponse;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopPageResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final OwnerService ownerService;
    private final RooftopService rooftopService;
    private final GreenBeeService greenBeeService;

    @GetMapping("/green-bees/waits")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 그린비 정보 가져오기",
            notes = "요청 데이터(param) - page : 0부터 시작")
    public APIResponse getWaitGreenBeeInfo(@RequestParam("page") int page) {
        Page<GreenBeeDto> greenBeeDtoPage = greenBeeService.getWaitGreenBeeWaits(page);

        return APIResponse.of(GreenBeePageResponse.of(
                greenBeeDtoPage.getTotalPages(),
                greenBeeDtoPage.getTotalElements(),
                GreenBeeInfoResponse.of(greenBeeDtoPage))
        );
    }

    @PostMapping("/green-bees/accept/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 그린비 승인",
            notes = "요청 데이터(path) - memberId : 승인할 memberId")
    public APIResponse acceptGreenBee(@PathVariable("memberId") long memberId) {
        greenBeeService.acceptGreenBee(memberId);

        return APIResponse.empty();
    }

    @DeleteMapping("/green-bees/reject/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 그린비 거절",
            notes = "요청 데이터(path) - memberId : 거절할 memberId")
    public APIResponse rejectGreenBee(@PathVariable("memberId") long memberId) {
        greenBeeService.rejectGreenBee(memberId);

        return APIResponse.empty();
    }

    @GetMapping("/owner/waits")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 옥상지기 정보 가져오기",
            notes = "요청 데이터(param) - page : 0부터 시작")
    public APIResponse getWaitOwnerInfo(@RequestParam("page") int page) {
        Page<OwnerDto> ownerDtoPage = ownerService.getWaitOwnerWaits(page);

        return APIResponse.of(OwnerPageResponse.of(
                ownerDtoPage.getTotalPages(),
                ownerDtoPage.getTotalElements(),
                OwnerInfoResponse.of(ownerDtoPage))
        );
    }

    @PostMapping("/owner/accept/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 옥상지기 승인",
            notes = "요청 데이터(path) - memberId : 승인할 memberId")
    public APIResponse acceptOwner(@PathVariable("memberId") long memberId) {
        ownerService.acceptGreenBee(memberId);
        
        return APIResponse.empty();
    }

    @DeleteMapping("/owner/reject/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 옥상지기 거절",
            notes = "요청 데이터(path) - memberId : 거절할 memberId")
    public APIResponse rejectOwner(@PathVariable("memberId") long memberId) {
        ownerService.rejectGreenBee(memberId);

        return APIResponse.empty();
    }

    @GetMapping("/rooftop/waits")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 옥상 정보 가져오기",
            notes = "요청 데이터(param) - page : 0부터 시작")
    public APIResponse getWaitRooftopInfo(@RequestParam("page") int page) {
        return APIResponse.of(
            RooftopPageResponse.of(rooftopService.getRooftopPageByProgress(page, Progress.ADMIN_WAIT), true));
    }

    @PostMapping("/rooftop/accept/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 옥상 승인",
            notes = "요청 데이터(path) - rooftopId : 승인할 rooftopId")
    public APIResponse acceptRooftop(@PathVariable("rooftopId") long rooftopId) {
        rooftopService.acceptRooftop(rooftopId);

        return APIResponse.empty();
    }

    @DeleteMapping("/rooftop/reject/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "승인 대기 중 옥상 거절",
            notes = "요청 데이터(path) - rooftopId : 거절할 rooftopId")
    public APIResponse rejectRooftop(@PathVariable("rooftopId") long rooftopId) {
        rooftopService.rejectRooftop(rooftopId);

        return APIResponse.empty();
    }

}
