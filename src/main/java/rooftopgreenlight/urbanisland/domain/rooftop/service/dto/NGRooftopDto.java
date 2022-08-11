package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;

import java.util.List;

@Data
public class NGRooftopDto {
    private Long id;
    private int widthPrice;
    private int requiredTermType;

    private String width;
    private String city;
    private String district;
    private String detail;
    private String phoneNumber;
    private String ownerContent;

    private List<RooftopImageDto> rooftopImages;
    private RooftopImageDto structureImage;

    protected NGRooftopDto(Long id, int widthPrice, String width, String city, String district,
                              String detail, List<RooftopImageDto> rooftopImages) {
        this.id = id;
        this.widthPrice = widthPrice;
        this.width = width;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.rooftopImages = rooftopImages;
    }

    protected NGRooftopDto(Long id, int widthPrice, int requiredTermType, String width, String city, String district,
                           String detail, String phoneNumber, String ownerContent,
                           List<RooftopImageDto> rooftopImages, RooftopImageDto structureImage) {
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

    public static NGRooftopDto of (Rooftop rooftop, List<RooftopImageDto> imageDtos) {
        return new NGRooftopDto(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(), imageDtos);
    }


    public static NGRooftopDto of(Long id, int widthPrice, int requiredTermType, String width, String city, String district, String detail, String phoneNumber, String ownerContent, List<RooftopImageDto> greenBeeImages, RooftopImageDto structureImage) {
        return new NGRooftopDto(id, widthPrice, requiredTermType, width, city, district, detail, phoneNumber, ownerContent, greenBeeImages, structureImage);
    }

}
