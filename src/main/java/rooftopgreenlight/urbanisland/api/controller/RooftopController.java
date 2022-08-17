package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.RooftopPageResponse;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopSearchCond;
import rooftopgreenlight.urbanisland.api.controller.dto.RooftopRequest;
import rooftopgreenlight.urbanisland.domain.common.Address;
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
            notes = "요청 데이터(Parameter) - key -> width, phoneNumber, explainContent, refundContent, roleContent, ownerContent," +
                    "startTime, endTime, totalPrice, widthPrice" +
                    "adultCount, kidCount, petCount, totalCount, county, city, detail, " +
                    "rooftopType(G, NG), normalFile, structureFile, mainFile, detailInfoNum, requiredItemNum, " +
                    "deadLineNum, optionContent, optionPrice " +
                    "<-> valueType : normalFile, structureFile, mainFile -> MultipartType")
    public APIResponse createRooftop(@PK Long memberId,
                                     @Validated RooftopRequest request,
                                     @RequestParam(name = "rooftopType") String rooftopType,
                                     @RequestParam(name = "normalFile", required = false) List<MultipartFile> normalFiles,
                                     @RequestParam(name = "structureFile", required = false) List<MultipartFile> structureFiles,
                                     @RequestParam(name = "mainFile", required = false) MultipartFile mainFile,
                                     @RequestParam(name = "detailInfoNum", required = false) List<Integer> detailInfos,
                                     @RequestParam(name = "requiredItemNum", required = false) List<Integer> requiredItems,
                                     @RequestParam(name = "deadLineNum", required = false) Integer deadLine,
                                     @RequestParam(name = "optionContent", required = false) List<String> options,
                                     @RequestParam(name = "optionPrice", required = false) List<Integer> prices,
                                     @RequestParam(name = "optionCount", required = false) List<Integer> counts
    ) {
        RooftopPeopleCount peopleCount
                = RooftopPeopleCount.of(request.getAdultCount(), request.getKidCount(), request.getPetCount(), request.getTotalCount());
        Address address
                = Address.of(request.getCounty(), request.getCity(), request.getDetail());

        rooftopService.createGreenRooftop(rooftopType, request.getWidth(), request.getPhoneNumber(), request.getExplainContent(),
                request.getRefundContent(), request.getRoleContent(), request.getOwnerContent(), request.getStartTime(),
                request.getEndTime(), request.getTotalPrice(), request.getWidthPrice(), peopleCount, address,
                normalFiles, structureFiles, mainFile, detailInfos, requiredItems, deadLine, options, prices, counts, memberId);

        return APIResponse.empty();

    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "옥상 검색 조건 조회",
            notes = "요청 데이터(Parameter) - key -> page, size, startTime, endTime, adultCount, kidCount," +
                    "petCount, city, district, maxPrice, minPrice, contentNum(list), maxWidth, minWidth, cond"
    )
    public APIResponse searchRooftop(@RequestParam int page, RooftopSearchCond searchCond) {
        return APIResponse.of(new RooftopPageResponse().RooftopSearchPageResponse(
            rooftopService.searchRooftopByCond(page, searchCond)
        ));
    }

}
