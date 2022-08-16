package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class RooftopDto {
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

    protected RooftopDto(Long id, String phoneNumber, String ownerContent, RooftopImageDto structureImage) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.ownerContent = ownerContent;
        this.structureImage = structureImage;
    }

    protected RooftopDto(Long id, int widthPrice, String width, String city, String district,
                         String detail, List<RooftopImageDto> rooftopImages) {
        this.id = id;
        this.widthPrice = widthPrice;
        this.width = width;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.rooftopImages = rooftopImages;
    }

    protected RooftopDto(Long id, int widthPrice, int requiredTermType, String width, String city, String district,
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

    public static RooftopDto of(Long id, String phoneNumber, String ownerContent, List<RooftopImage> rooftopImages) {
        return new RooftopDto(
                id,
                phoneNumber,
                ownerContent,
                RooftopImageDto.of(
                rooftopImages
                    .stream()
                    .filter(rooftopImage -> rooftopImage.getRooftopImageType() == ImageType.STRUCTURE)
                    .findFirst()
                    .orElse(null)
                )
        );
    }

    public static RooftopDto of (Rooftop rooftop, List<RooftopImageDto> imageDtos) {
        return new RooftopDto(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(), imageDtos);
    }

    public static RooftopDto of(Long id, int widthPrice, Integer requiredTermType, String width, String city, String district, String detail, String phoneNumber, String ownerContent) {
        return new RooftopDto(id, widthPrice, requiredTermType, width, city, district, detail, phoneNumber, ownerContent, null, null);
    }

    public static RooftopDto of(Rooftop rooftop, boolean isOne) {
        RooftopDto rooftopDto = isOne ? createDetailNGRooftopDto(rooftop) : createListInfoNGRooftopDto(rooftop);

        Map<Boolean, List<RooftopImageDto>> listMap = rooftop.getRooftopImages().stream().map(RooftopImageDto::of)
                .collect(Collectors.partitioningBy(rooftopImageResponse -> rooftopImageResponse.getRooftopImageType() == ImageType.NORMAL));

        rooftopDto.setRooftopImages(listMap.get(true));
        if (listMap.get(false) != null) {
            rooftopDto.setStructureImage(listMap.get(false).get(0));
        }

        return rooftopDto;
    }

    private static RooftopDto createListInfoNGRooftopDto(Rooftop rooftop) {
        return new RooftopDto(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(), null);
    }

    private static RooftopDto createDetailNGRooftopDto(Rooftop rooftop) {
        return RooftopDto.of(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getDeadLineType(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(),
                rooftop.getPhoneNumber(), rooftop.getOwnerContent());
    }

}
