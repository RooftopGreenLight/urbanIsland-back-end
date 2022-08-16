package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopPageDto {

    private int totalPages;
    private long totalElements;
    private List<RooftopDto> rooftopResponses;

    public static RooftopPageDto of(int totalPages, long totalElements, List<Rooftop> rooftops, boolean isAdmin) {
        if (isAdmin) {
            return new RooftopPageDto(
                    totalPages,
                    totalElements,
                    rooftops.stream().map(rooftop -> RooftopDto.of(
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
                rooftops.stream().map(rooftop -> RooftopDto.of(rooftop, false)).collect(Collectors.toList())
        );
    }
}
