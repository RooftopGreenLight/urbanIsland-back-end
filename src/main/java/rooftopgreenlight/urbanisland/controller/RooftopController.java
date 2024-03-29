package rooftopgreenlight.urbanisland.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.config.annotation.PK;
import rooftopgreenlight.urbanisland.domain.model.Address;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopPeopleCount;
import rooftopgreenlight.urbanisland.service.RooftopService;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopSearchCond;
import rooftopgreenlight.urbanisland.dto.APIResponse;
import rooftopgreenlight.urbanisland.dto.rooftop.ReviewRequest;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopEditRequest;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopPageResponse;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopRequest;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopResponse;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopReviewPageResponse;

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
                = Address.of(request.getCity(), request.getDistrict(), request.getDetail());

        rooftopService.createGreenRooftop(rooftopType, request.getWidth(), request.getPhoneNumber(), request.getExplainContent(),
                request.getRefundContent(), request.getRoleContent(), request.getOwnerContent(), request.getStartTime(),
                request.getEndTime(), request.getTotalPrice(), request.getWidthPrice(), peopleCount, address,
                normalFiles, structureFiles, mainFile, detailInfos, requiredItems, deadLine, options, prices, counts, memberId);

        return APIResponse.empty();

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "내 옥상 관리하기",
            notes = "요청 데이터(param) - key : page, size)")
    public APIResponse createOwner(@PK Long memberId,
                                   @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
                                   ){
        rooftopService.getMyRooftopInfo(memberId, pageable);

        return APIResponse.of(new RooftopPageResponse().RooftopSearchPageResponse(
                rooftopService.getMyRooftopInfo(memberId, pageable), "G")
        );
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "옥상 검색 조건 조회",
            notes = "요청 데이터(Parameter) - key -> page, size, startTime, endTime, adultCount, kidCount," +
                    "petCount, city, district, maxPrice, minPrice, contentNum(list), maxWidth, minWidth, " +
                    "minWidthPrice, maxWidthPrice, deadLineType, cond, type(G, NG)"
    )
    public APIResponse searchRooftop(
            @RequestParam int page,
            @RequestParam String type,
            RooftopSearchCond searchCond
            ) {
        return APIResponse.of(new RooftopPageResponse().RooftopSearchPageResponse(
            rooftopService.searchRooftopByCond(page, type, searchCond), type
        ));
    }

    @GetMapping("/reviews")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "나의 옥상 리뷰 조회",
            notes = "요청 데이터(parameter) - key -> page"
    )
    public APIResponse deleteRooftopReview(
            @PK Long memberId,
            @RequestParam int page
    ) {
        return APIResponse.of(RooftopReviewPageResponse.from(rooftopService.findByMyRooftopReview(memberId, page)));
    }

    @PostMapping("/reviews/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "옥상 리뷰 등록",
            notes = "요청 데이터(path, requestBody) - key -> rooftopId(path)" +
                    "content(body), grade(body)"
    )
    public APIResponse createRooftopReview(
            @PK Long memberId,
            @PathVariable("rooftopId") Long rooftopId,
            @RequestBody @Validated ReviewRequest dto
    ) {
        rooftopService.createReview(memberId, rooftopId, dto.getContent(), dto.getGrade());

        return APIResponse.createEmpty();
    }

    @DeleteMapping("/reviews/{rooftopId}/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "옥상 리뷰 삭제",
            notes = "요청 데이터(path) - key -> rooftopId, reviewId"
    )
    public APIResponse deleteRooftopReview(
            @PK Long memberId,
            @PathVariable("rooftopId") Long rooftopId,
            @PathVariable("reviewId") Long reviewId
    ) {
        rooftopService.deleteReview(memberId, rooftopId, reviewId);

        return APIResponse.empty();
    }


    @GetMapping("/detail/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "(3) reservation detail page - 옥상 id에 따른 옥상 detail 내용 조회",
            notes = "요청 데이터(path) - key -> rooftopId"
    )
    public APIResponse getRooftop(@PathVariable("rooftopId") Long rooftopId) {
        return APIResponse.of(RooftopResponse.getRooftopDetail(rooftopService.getRooftopDetail(rooftopId, "G")));
    }

    @PatchMapping("/detail/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(11-1) 옥상 수정하기",
                notes = "요청 데이터(path) - key -> rooftopId \n" +
                        "요청 데이터(form-data) - key -> addImages, deleteImages, mainImage, adultCount, kidCount, petCount, totalCount, startTime, endTime, totalPrice")
    public APIResponse editRooftop(@PathVariable("rooftopId") Long rooftopId,
                                   @PK Long memberId,
                                   @RequestParam(value = "addImages", required = false) List<MultipartFile> addImages,
                                   @RequestParam(value = "deleteImages", required = false) List<String> deleteFileNames,
                                   @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
                                   RooftopEditRequest editRequest) {
        rooftopService.editRooftopDetail(rooftopId, memberId, addImages, deleteFileNames, mainImage, editRequest.getAdultCount(),
                editRequest.getKidCount(), editRequest.getPetCount(), editRequest.getTotalCount(), editRequest.getStartTime(),
                editRequest.getEndTime(), editRequest.getTotalPrice());
        return APIResponse.empty();
    }

    @PostMapping("/detail/option/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(11-1-1) pay option change",
            notes = "요청 데이터(path) - key -> rooftopId \n" +
                    "요청 데이터(form-data) - key -> optionContent, optionPrice, optionCount")
    public APIResponse editRooftopOptions(@PathVariable("rooftopId") Long rooftopId,
                                          @PK Long memberId,
                                          @RequestParam(value = "optionContent", required = false) List<String> contents,
                                          @RequestParam(value = "optionPrice", required = false) List<Integer> prices,
                                          @RequestParam(value = "optionCount", required = false) List<Integer> counts) {
        rooftopService.editRooftopOption(rooftopId, memberId, contents, prices, counts);
        return APIResponse.empty();
    }

}
