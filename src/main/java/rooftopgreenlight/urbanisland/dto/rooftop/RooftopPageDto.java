package rooftopgreenlight.urbanisland.dto.rooftop;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.file.ImageType;
import rooftopgreenlight.urbanisland.domain.rooftop.Rooftop;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopPageDto {

    private int totalPages;
    private long totalElements;
    private List<RooftopDto> rooftopResponses;

    public RooftopPageDto RooftopSearchPageDto(int totalPages, long totalElements, List<Rooftop> rooftops, String type) {
        if(type.equals("G")) {
            return new RooftopPageDto(
                    totalPages,
                    totalElements,
                    rooftops.stream().map(rooftop ->
                            new RooftopDto().RooftopSearchResultDto(
                                    rooftop.getId(),
                                    rooftop.getAddress().getCity(),
                                    rooftop.getAddress().getDistrict(),
                                    rooftop.getAddress().getDetail(),
                                    rooftop.getGrade(),
                                    rooftop.getTotalPrice(),
                                    rooftop.getRooftopImages().stream()
                                            .filter(rooftopImage -> rooftopImage.getRooftopImageType() == ImageType.MAIN)
                                            .findFirst()
                                            .orElse(null)
                            )).collect(Collectors.toList())
            );
        }
        return new RooftopPageDto(totalPages, totalElements,
                rooftops.stream().map(rooftop ->
                    new RooftopDto().NGRooftopSearchResultDto(
                            rooftop.getId(),
                            rooftop.getAddress().getCity(),
                            rooftop.getAddress().getDistrict(),
                            rooftop.getAddress().getDetail(),
                            rooftop.getWidth(),
                            rooftop.getWidthPrice(),
                            rooftop.getRooftopImages().stream()
                                    .filter(rooftopImage -> rooftopImage.getRooftopImageType() == ImageType.MAIN)
                                    .findFirst()
                                    .orElse(null)
                    )).collect(Collectors.toList()));
    }

    public static RooftopPageDto of(int totalPages, long totalElements, List<Rooftop> rooftops, boolean isAdmin) {
        if (isAdmin) {
            return new RooftopPageDto(
                    totalPages,
                    totalElements,
                    rooftops.stream().map(rooftop -> RooftopDto.getAdminNGRooftopDto(
                            rooftop.getId(),
                            rooftop.getPhoneNumber(),
                            rooftop.getOwnerContent(),
                            rooftop.getRooftopImages()
                    )).collect(Collectors.toList())
            );
        }

        return new RooftopPageDto(
                totalPages,
                totalElements,
                rooftops.stream().map(rooftop -> RooftopDto.getNGRooftopDto(rooftop, false)).collect(Collectors.toList())
        );
    }
}
