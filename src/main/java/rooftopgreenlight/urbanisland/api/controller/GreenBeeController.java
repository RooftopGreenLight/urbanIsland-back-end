package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.*;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.GreenBee;
import rooftopgreenlight.urbanisland.domain.greenbee.service.GreenBeeService;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.service.RooftopService;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.NGRooftopDto;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopPageDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/green-bees")
public class GreenBeeController {

    private final RooftopService rooftopService;
    private final GreenBeeService greenBeeService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "그린비 등록하기",
            notes = "요청 데이터(form-data) - key : officeNumber(NotBlank), content(NotBlank), city(NotBlank), district(NotBlank)," +
                    " detail(form-data), normalFile(Multipart), confirmationFile(Multipart)")
    public APIResponse joinGreenBee(@PK Long memberId,
                                    @Validated GreenBeeRequest request,
                                    @RequestParam(name = "normalFile", required = false) List<MultipartFile> normalFiles,
                                    @RequestParam(name = "confirmationFile", required = false) MultipartFile confirmationFile) {

        greenBeeService.saveGreenBee(memberId, request.getOfficeNumber(), request.getContent(),
                Address.of(request.getCity(), request.getDistrict(), request.getDetail()), normalFiles, confirmationFile);

        return APIResponse.createEmpty();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "그린비 정보 가져오기",
            notes = "요청 데이터 - 없음")
    public APIResponse getMyGreenBeeInfo(@PK Long memberId) {
        GreenBee greenBeeInfo = greenBeeService.getMyGreenBeeInfo(memberId);

        return APIResponse.of(GreenBeeInfoResponse.of(
                greenBeeInfo,
                greenBeeInfo.getGreenBeeImages().stream().map(GreenBeeImageResponse::of).collect(Collectors.toList())
        ));
    }

    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "타인의 그린비 정보 가져오기",
            notes = "요청 데이터(path) - /memberId")
    public APIResponse getOtherGreenBeeInfo(@PathVariable("memberId") Long memberId) {

        GreenBee greenBeeInfo = greenBeeService.getMyGreenBeeInfo(memberId);
        return APIResponse.of(GreenBeeInfoResponse.of(
                greenBeeInfo,
                greenBeeInfo.getGreenBeeImages().stream().map(GreenBeeImageResponse::of).collect(Collectors.toList())
        ));
    }

    // 마이페이지(그린비) - 녹화가 필요한 옥상 찾기
    @GetMapping("/required-green")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse getRequiredGreenRooftop(@RequestParam("page") int page) {
        RooftopPageDto ngRooftopPageDto = rooftopService.getNGRooftop(page);

        return APIResponse.of(RooftopPageResponse.of(ngRooftopPageDto));
    }

    // 녹화가 필요한 옥상 찾기 - 각 옥상 클릭
    @GetMapping("/required-green/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse getRequiredGreenRooftopDetail(@PathVariable("rooftopId") Long rooftopId) {
        NGRooftopDto ngRooftopDto = rooftopService.getNGRooftopDetail(rooftopId);

        return APIResponse.of(RooftopResponse.of(ngRooftopDto, true));
    }

}
