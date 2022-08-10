package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.GreenBeeImageResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.GreenBeeInfoResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.GreenBeeRequest;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.GreenBee;
import rooftopgreenlight.urbanisland.domain.greenbee.service.GreenBeeService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/green-bees")
public class GreenBeeController {

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

}
