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
    private List<NGRooftopDto> rooftopResponses;

    public static RooftopPageDto of(int totalPages, long totalElements, List<Rooftop> rooftops) {
        return new RooftopPageDto(
                totalPages,
                totalElements,
                rooftops.stream().map(rooftop -> NGRooftopDto.of(rooftop, false)).collect(Collectors.toList())
        );
    }
}
