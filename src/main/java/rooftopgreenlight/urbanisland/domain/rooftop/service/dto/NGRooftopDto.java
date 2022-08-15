package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.Data;
import rooftopgreenlight.urbanisland.api.controller.dto.RooftopImageResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.RooftopResponse;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopOption;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class NGRooftopDto {
    private Long id;
    private int widthPrice;
    private Integer requiredTermType;

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


    public static NGRooftopDto of(Long id, int widthPrice, Integer requiredTermType, String width, String city, String district, String detail, String phoneNumber, String ownerContent) {
        return new NGRooftopDto(id, widthPrice, requiredTermType, width, city, district, detail, phoneNumber, ownerContent, null, null);
    }

    public static NGRooftopDto of(Rooftop rooftop, boolean isOne) {
        NGRooftopDto ngRooftopDto = isOne ? createDetailNGRooftopDto(rooftop) : createListInfoNGRooftopDto(rooftop);

        Map<Boolean, List<RooftopImageDto>> listMap = rooftop.getRooftopImages().stream().map(RooftopImageDto::of)
                .collect(Collectors.partitioningBy(rooftopImageResponse -> rooftopImageResponse.getRooftopImageType() == ImageType.NORMAL));

        ngRooftopDto.setRooftopImages(listMap.get(true));
        if (listMap.get(false) != null) {
            ngRooftopDto.setStructureImage(listMap.get(false).get(0));
        }

        return ngRooftopDto;
    }

    private static NGRooftopDto createListInfoNGRooftopDto(Rooftop rooftop) {
        return new NGRooftopDto(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(), null);
    }

    private static NGRooftopDto createDetailNGRooftopDto(Rooftop rooftop) {
        return NGRooftopDto.of(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getDeadLineType(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(),
                rooftop.getPhoneNumber(), rooftop.getOwnerContent());
    }

}
