package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopPageDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopPageResponse {

    private int totalPages;
    private long totalElements;
    private List<RooftopResponse> rooftopResponses;

    public static RooftopPageResponse of(RooftopPageDto rooftopPageDto) {
        return new RooftopPageResponse(
                rooftopPageDto.getTotalPages(),
                rooftopPageDto.getTotalElements(),
                rooftopPageDto.getRooftopResponses().stream().map(rooftopDto -> RooftopResponse.of(rooftopDto, false))
                        .collect(Collectors.toList())
        );
    }

}