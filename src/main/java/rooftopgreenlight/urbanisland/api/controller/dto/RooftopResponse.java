package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.NGRooftopDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RooftopResponse {
    private Long id;

    // 이미지, 면적, 가격, 주소여서 이거 보내주고 클릭하면 나오는 건
    // 주소, 연락처, 넓이, 희망가격대, 필요항목, 필요시공기한, 그린비멘트. 옥상대표사진, 건물단면도
    private int widthPrice;
    private int requiredTermType;

    private String width;
    private String city;
    private String district;
    private String detail;
    private String phoneNumber;
    private String ownerContent;

    private List<RooftopImageResponse> rooftopImages;
    private RooftopImageResponse structureImage;

    protected RooftopResponse(Long id, int widthPrice, String width, String city, String district,
                              String detail, List<RooftopImageResponse> rooftopImages) {
        this.id = id;
        this.widthPrice = widthPrice;
        this.width = width;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.rooftopImages = rooftopImages;
    }

    protected RooftopResponse(Long id, int widthPrice, int requiredTermType, String width, String city, String district,
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

    public static RooftopResponse of(Rooftop rooftop, List<RooftopImageResponse> greenBeeImages) {
        return new RooftopResponse(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(), greenBeeImages);
    }

    public static RooftopResponse of(NGRooftopDto ngRooftop) {
        List<RooftopImageResponse> rooftopImages = ngRooftop.getRooftopImages().stream().map(RooftopImageResponse::of).collect(Collectors.toList());
        RooftopImageResponse structureImage = RooftopImageResponse.of(ngRooftop.getStructureImage());

        return new RooftopResponse(ngRooftop.getId(), ngRooftop.getWidthPrice(), ngRooftop.getRequiredTermType(),
                ngRooftop.getWidth(), ngRooftop.getCity(), ngRooftop.getDistrict(), ngRooftop.getDetail(),
                ngRooftop.getPhoneNumber(), ngRooftop.getOwnerContent(), rooftopImages, structureImage);
    }


}
