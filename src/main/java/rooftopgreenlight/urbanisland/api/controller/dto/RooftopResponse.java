package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopDto;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopImageDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RooftopResponse {
    private Long id;

    // 이미지, 면적, 가격, 주소여서 이거 보내주고 클릭하면 나오는 건
    // 주소, 연락처, 넓이, 희망가격대, 필요항목, 필요시공기한, 그린비멘트. 옥상대표사진, 건물단면도
    private Integer totalPrice;
    private Integer widthPrice;
    private Integer requiredTermType;

    private Integer adultCount;
    private Integer kidCount;
    private Integer petCount;
    private Integer totalCount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;

    private String grade;
    private Double width;
    private String city;
    private String district;
    private String detail;
    private String phoneNumber;
    private String explainContent;
    private String refundContent;
    private String roleContent;
    private String ownerContent;

    private String progress;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime rooftopDate;

    private List<Integer> detailNums;
    private List<RooftopImageResponse> rooftopImages;
    private RooftopImageResponse structureImage;
    private RooftopImageResponse mainImage;
    private List<RooftopReviewResponse> rooftopReviews;

    protected RooftopResponse(Long id, String city, String district, String detail, String progress, LocalDateTime rooftopDate) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.progress = progress;
        this.rooftopDate = rooftopDate;
    }

    protected RooftopResponse(Long id, String phoneNumber, String ownerContent, RooftopImageResponse structureImage) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.ownerContent = ownerContent;
        this.structureImage = structureImage;
    }

    protected RooftopResponse(Long id, int widthPrice, Double width, String city, String district,
                              String detail, List<RooftopImageResponse> rooftopImages) {
        this.id = id;
        this.widthPrice = widthPrice;
        this.width = width;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.rooftopImages = rooftopImages;
    }

    protected RooftopResponse(Long id, int widthPrice, int requiredTermType, Double width, String city, String district,
                              String detail, String phoneNumber, String ownerContent, List<RooftopImageResponse> rooftopImages,
                              RooftopImageResponse structureImage) {
        this.id = id;
        this.widthPrice = widthPrice;
        this.requiredTermType = requiredTermType;
        this.width = width;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.phoneNumber = phoneNumber;
        this.ownerContent = ownerContent;
        this.rooftopImages = rooftopImages;
        this.structureImage = structureImage;
    }

    protected RooftopResponse(Long id, String city, String district, String detail, String grade, Integer totalPrice, RooftopImageDto mainImage) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.grade = grade;
        this.totalPrice = totalPrice;
        this.mainImage = RooftopImageResponse.of(mainImage);
    }

    protected RooftopResponse(Long id, String city, String district, String detail,
                              Double width, Integer widthPrice, RooftopImageDto mainImage) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.width = width;
        this.widthPrice = widthPrice;
        this.mainImage = RooftopImageResponse.of(mainImage);
    }


    protected RooftopResponse(Long id, Integer totalPrice, String city, String district, String detail, String explainContent, String roleContent,
                         String refundContent, String grade, Double width, List<RooftopImageResponse> rooftopImages, RooftopImageResponse structureImage,
                         Integer adultCount, Integer kidCount, Integer petCount, Integer totalCount, List<Integer> detailNums,
                         LocalTime startTime, LocalTime endTime, List<RooftopReviewResponse> reviewDtos) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.explainContent = explainContent;
        this.roleContent = roleContent;
        this.refundContent = refundContent;
        this.grade = grade;
        this.width = width;
        this.rooftopImages = rooftopImages;
        this.structureImage = structureImage;
        this.adultCount = adultCount;
        this.kidCount = kidCount;
        this.petCount = petCount;
        this.totalCount = totalCount;
        this.detailNums = detailNums;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rooftopReviews = reviewDtos;
    }


    public RooftopResponse rooftopSearchResultResponse(Long id, String city, String district, String detail,
                                                       String grade, Integer totalPrice, RooftopImageDto mainImage) {
        return new RooftopResponse(id, city, district, detail, grade, totalPrice, mainImage);
    }

    public RooftopResponse ngRooftopSearchResultResponse(Long id, String city, String district, String detail,
                                                         Double width, Integer widthPrice, RooftopImageDto mainImage) {
        return new RooftopResponse(id, city, district, detail, width, widthPrice, mainImage);
    }

    public static RooftopResponse of(Long id, String phoneNumber, String ownerContent, RooftopImageDto structureImage) {
        return new RooftopResponse(id, phoneNumber, ownerContent, RooftopImageResponse.of(structureImage));
    }

    public static RooftopResponse of(RooftopDto rooftopDto, boolean isOne) {
        if (isOne) {
            return new RooftopResponse(rooftopDto.getId(), rooftopDto.getWidthPrice(), rooftopDto.getRequiredTermType(), rooftopDto.getWidth(),
                    rooftopDto.getCity(), rooftopDto.getDistrict(), rooftopDto.getDetail(), rooftopDto.getPhoneNumber(),
                    rooftopDto.getOwnerContent(), rooftopDto.getRooftopImages().stream().map(RooftopImageResponse::of).collect(Collectors.toList()),
                    RooftopImageResponse.of(rooftopDto.getStructureImage()));
        }

        return new RooftopResponse(rooftopDto.getId(), rooftopDto.getCity(), rooftopDto.getDistrict(), rooftopDto.getDetail(),
                rooftopDto.getWidth(), rooftopDto.getWidthPrice(),  rooftopDto.getRooftopImages().get(0));
    }

    public static RooftopResponse getRooftopDetail(RooftopDto rooftopDto) {
        List<RooftopImageResponse> rooftopImages = rooftopDto.getRooftopImages().stream().map(RooftopImageResponse::of).collect(Collectors.toList());
        RooftopImageResponse structureImage = RooftopImageResponse.of(rooftopDto.getStructureImage());
        List<RooftopReviewResponse> reviews = rooftopDto.getRooftopReviews().stream().map(RooftopReviewResponse::of).collect(Collectors.toList());

        return new RooftopResponse(rooftopDto.getId(), rooftopDto.getTotalPrice(), rooftopDto.getCity(), rooftopDto.getDistrict(),
                rooftopDto.getDetail(), rooftopDto.getExplainContent(), rooftopDto.getRoleContent(), rooftopDto.getRefundContent(),
                rooftopDto.getGrade(), rooftopDto.getWidth(), rooftopImages, structureImage,
                rooftopDto.getAdultCount(), rooftopDto.getKidCount(), rooftopDto.getPetCount(), rooftopDto.getTotalCount(),
                rooftopDto.getDetailNums(), rooftopDto.getStartTime(), rooftopDto.getEndTime(), reviews);
    }

    public static List<RooftopResponse> getRooftopStatus(List<RooftopDto> rooftopDtos) {
        List<RooftopResponse> responses = new ArrayList<>();
        if(rooftopDtos != null)
            rooftopDtos.forEach(rooftopDto -> {
                responses.add(new RooftopResponse(rooftopDto.getId(), rooftopDto.getCity(), rooftopDto.getDistrict(),
                        rooftopDto.getDetail(), rooftopDto.getProgress(), rooftopDto.getRooftopDate()));
            });
        return responses;
    }

}
