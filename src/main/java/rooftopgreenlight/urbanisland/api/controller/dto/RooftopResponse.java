package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopDto;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopImageDto;

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

    private Double grade;
    private Double width;
    private String city;
    private String district;
    private String detail;
    private String phoneNumber;
    private String ownerContent;

    private List<RooftopImageResponse> rooftopImages;
    private RooftopImageResponse structureImage;

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

    protected RooftopResponse(Long id, String city, String district, String detail, Double grade, Integer totalPrice, RooftopImageDto structureImage) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.grade = grade;
        this.totalPrice = totalPrice;
        this.structureImage = RooftopImageResponse.of(structureImage);
    }

    public RooftopResponse RooftopSearchResultResponse(Long id, String city, String district, String detail,
                                             Double grade, Integer totalPrice, RooftopImageDto structureImage) {
        return new RooftopResponse(id, city, district, detail, grade, totalPrice, structureImage);
    }

    public static RooftopResponse of(Long id, String phoneNumber, String ownerContent, RooftopImageDto structureImage) {
        return new RooftopResponse(id, phoneNumber, ownerContent, RooftopImageResponse.of(structureImage));
    }

    public static RooftopResponse of(Rooftop rooftop, List<RooftopImageResponse> greenBeeImages) {
        return new RooftopResponse(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(), greenBeeImages);
    }

    public static RooftopResponse of(RooftopDto rooftopDto, boolean isOne) {
        if (isOne) {
            return new RooftopResponse(rooftopDto.getId(), rooftopDto.getWidthPrice(), 3, rooftopDto.getWidth(),
                    rooftopDto.getCity(), rooftopDto.getDistrict(), rooftopDto.getDetail(), rooftopDto.getPhoneNumber(),
                    rooftopDto.getOwnerContent(), rooftopDto.getRooftopImages().stream().map(RooftopImageResponse::of).collect(Collectors.toList()),
                    RooftopImageResponse.of(rooftopDto.getStructureImage()));
        }

            return new RooftopResponse(rooftopDto.getId(), rooftopDto.getWidthPrice(), rooftopDto.getWidth(),
                    rooftopDto.getCity(), rooftopDto.getDistrict(), rooftopDto.getDetail(),
                    rooftopDto.getRooftopImages().stream().map(RooftopImageResponse::of).collect(Collectors.toList()));
    }
}
