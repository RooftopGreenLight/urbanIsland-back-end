package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.controller.dto.*;
import rooftopgreenlight.urbanisland.domain.greenbee.service.GreenBeeService;
import rooftopgreenlight.urbanisland.domain.greenbee.service.dto.GreenBeeDto;
import rooftopgreenlight.urbanisland.domain.owner.service.OwnerService;
import rooftopgreenlight.urbanisland.domain.owner.service.dto.OwnerDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final OwnerService ownerService;
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

}
