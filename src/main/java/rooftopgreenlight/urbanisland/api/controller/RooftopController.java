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
import rooftopgreenlight.urbanisland.api.controller.dto.RooftopRequest;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopAddress;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopPeopleCount;
import rooftopgreenlight.urbanisland.domain.rooftop.service.RooftopService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooftops")
public class RooftopController {

    private final RooftopService rooftopService;

    @PostMapping("/green")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Green Rooftop 저장",
            notes = "요청 데이터(Parameter) -> key : width, explainContent, refundContent, roleContent, startTime, endTime, " +
                    "adultCount, kidCount, petCount, totalCount, county, city, detail, normalFile, structureFile, detailNum, optionContent, " +
                    "optionPrice <-> valueType : normalFile, structureFile -> MultipartType")
    public APIResponse createRooftop(@PK Long memberId,
                                     @Validated RooftopRequest request,
                                     @RequestParam(name = "normalFile", required = false) List<MultipartFile> normalFiles,
                                     @RequestParam(name = "structureFile", required = false) List<MultipartFile> structureFiles,
                                     @RequestParam(name = "detailNum", required = false) List<Integer> details,
                                     @RequestParam(name = "optionContent", required = false) List<String> options,
                                     @RequestParam(name = "optionPrice", required = false) List<Integer> prices
    ) {

        RooftopPeopleCount peopleCount
                = RooftopPeopleCount.of(request.getAdultCount(), request.getKidCount(), request.getPetCount(), request.getTotalCount());
        RooftopAddress rooftopAddress
                = RooftopAddress.of(request.getCounty(), request.getCity(), request.getDetail());

        rooftopService.createGreenRooftop(request.getWidth(), request.getExplainContent(), request.getRefundContent(),
                request.getRoleContent(), request.getStartTime(), request.getEndTime(), peopleCount, rooftopAddress,
                normalFiles, structureFiles, details, options, prices, memberId);

        return APIResponse.empty();

    }

}
