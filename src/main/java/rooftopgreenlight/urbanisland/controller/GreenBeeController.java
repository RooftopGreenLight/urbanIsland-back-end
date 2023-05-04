package rooftopgreenlight.urbanisland.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.config.annotation.PK;
import rooftopgreenlight.urbanisland.domain.model.Address;
import rooftopgreenlight.urbanisland.domain.greenbee.GreenBee;
import rooftopgreenlight.urbanisland.service.GreenBeeService;
import rooftopgreenlight.urbanisland.service.RooftopGreeningApplyService;
import rooftopgreenlight.urbanisland.service.RooftopService;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopDto;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopPageDto;
import rooftopgreenlight.urbanisland.dto.APIResponse;
import rooftopgreenlight.urbanisland.dto.greenbee.GreenBeeImageResponse;
import rooftopgreenlight.urbanisland.dto.greenbee.GreenBeeInfoResponse;
import rooftopgreenlight.urbanisland.dto.greenbee.GreenBeeRequest;
import rooftopgreenlight.urbanisland.dto.greenbee.GreeningApplyResponse;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopPageResponse;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/green-bees")
public class GreenBeeController {

    private final RooftopService rooftopService;
    private final GreenBeeService greenBeeService;
    private final RooftopGreeningApplyService greeningApplyService;

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
            notes = "요청 데이터(path) - key -> memberId")
    public APIResponse getOtherGreenBeeInfo(@PathVariable("memberId") Long memberId) {

        GreenBee greenBeeInfo = greenBeeService.getMyGreenBeeInfo(memberId);
        return APIResponse.of(GreenBeeInfoResponse.of(
                greenBeeInfo,
                greenBeeInfo.getGreenBeeImages().stream().map(GreenBeeImageResponse::of).collect(Collectors.toList())
        ));
    }

    @GetMapping("/required-green")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "마이페이지(그린비) - 녹화가 필요한 옥상 찾기",
            notes = "요청 데이터(Parameter) - key -> page")
    public APIResponse getRequiredGreenRooftop(@RequestParam("page") int page) {
        RooftopPageDto ngRooftopPageDto = rooftopService.getNGRooftop(page);

        return APIResponse.of(new RooftopPageResponse().RooftopSearchPageResponse(ngRooftopPageDto, "NG"));
    }

    @GetMapping("/required-green/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "녹화가 필요한 옥상 찾기 - 개별 NG 옥상 정보 조회",
        notes = "요청 데이터(path) - key -> rooftopId")
    public APIResponse getRequiredGreenRooftopDetail(@PathVariable("rooftopId") Long rooftopId) {
        RooftopDto rooftopDto = rooftopService.getRooftopDetail(rooftopId, "NG");

        return APIResponse.of(RooftopResponse.of(rooftopDto, true));
    }

    @GetMapping("/required-green/select/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "그린비 -> 옥상 녹화 신청하기 ",
        notes = "요청 데이터(path) - key -> rooftopId")
    public APIResponse selectRequiredGreenRooftop(@PathVariable("rooftopId") Long rooftopId,
                                                  @PK Long memberId) {
        rooftopService.selectGreenBeeNGRooftop(rooftopId, memberId);
        return APIResponse.empty();
    }

    @GetMapping("/greening-rooftop")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(16) 본인을 선택한 옥상 확인하기 - 녹화 중인 옥상",
        notes = "요청 데이터 - 없음")
    public APIResponse getGreeningRooftop(@PK Long memberId) {
        return APIResponse.of(
            GreeningApplyResponse.of(greeningApplyService.getRooftopOfGreenBee(memberId, "ACCEPTED"), "ACCEPTED"));
    }

    @GetMapping("/greening-rooftop/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(16) 본인을 선택한 옥상 확인하기 - 녹화 확정하기",
        notes = "요청 데이터(path) - key -> rooftopId")
    public APIResponse completeGreeningRooftop(@PathVariable(value = "rooftopId") Long rooftopId,
                                               @PK Long memberId) {
        rooftopService.completeGreeningRooftop(rooftopId, memberId);
        return APIResponse.empty();
    }

    @GetMapping("/greening-select-rooftop")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(16) 본인을 선택한 옥상 확인하기 - 녹화를 신청한 옥상",
        notes = "요청 데이터 - 없음")
    public APIResponse getSelectedRooftop(@PK Long memberId) {
        return APIResponse.of(GreeningApplyResponse.of(greeningApplyService.getRooftopOfGreenBee(memberId, "SELECTED"), "SELECTED"));
    }

    @GetMapping("/greening-completed-rooftop")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(16) 본인을 선택한 옥상 확인하기 - 녹화를 완료한 옥상",
        notes = "요청 데이터 - 없음")
    public APIResponse getCompletedRooftop(@PK Long memberId) {
        return APIResponse.of(GreeningApplyResponse.of(greeningApplyService.getRooftopOfGreenBee(memberId, "COMPLETED"), "COMPLETED"));
    }

    @PostMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(18) 그린비 페이지 수정",
        notes = "요청 데이터(form-data) - key : officeNumber, content, deleteImages(String), addImages(MultipartFile)")
    public APIResponse editGreenBeePage(@PK Long memberId,
                                        @RequestParam(name = "officeNumber", required = false) String officeNumber,
                                        @RequestParam(name = "content", required = false) String content,
                                        @RequestParam(name = "deleteImages", required = false) List<String> deleteImages,
                                        @RequestParam(name = "addImages", required = false) List<MultipartFile> addImages) {
        greenBeeService.editGreenBeeInfo(memberId, officeNumber, content, deleteImages, addImages);
        return APIResponse.empty();
    }

}
