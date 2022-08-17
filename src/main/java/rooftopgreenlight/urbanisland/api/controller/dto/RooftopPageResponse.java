package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopDto;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopPageDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopPageResponse {

    private int totalPages;
    private long totalElements;
    private List<RooftopResponse> rooftopResponses;

    public RooftopPageResponse RooftopSearchPageResponse(RooftopPageDto rooftopPageDto) {
        return new RooftopPageResponse(
                rooftopPageDto.getTotalPages(),
                rooftopPageDto.getTotalElements(),
                rooftopPageDto.getRooftopResponses().stream().map(rooftopDto ->
                        new RooftopResponse().RooftopSearchResultResponse(
                                rooftopDto.getId(),
                                rooftopDto.getCity(),
                                rooftopDto.getDistrict(),
                                rooftopDto.getDetail(),
                                rooftopDto.getGrade(),
                                rooftopDto.getTotalPrice(),
                                rooftopDto.getStructureImage()
                        )).collect(Collectors.toList())
        );
    }

    public static RooftopPageResponse of(RooftopPageDto rooftopPageDto, boolean isAdmin) {
        if (isAdmin) {
            return new RooftopPageResponse(
                rooftopPageDto.getTotalPages(),
                rooftopPageDto.getTotalElements(),
                rooftopPageDto.getRooftopResponses().stream().map(rooftopDto -> RooftopResponse.of(
                            rooftopDto.getId(),
                            rooftopDto.getPhoneNumber(),
                            rooftopDto.getOwnerContent(),
                            rooftopDto.getStructureImage()
                    ))
                    .collect(Collectors.toList())
            );
        }

        return new RooftopPageResponse(
                rooftopPageDto.getTotalPages(),
                rooftopPageDto.getTotalElements(),
                rooftopPageDto.getRooftopResponses().stream().map(rooftopDto -> RooftopResponse.of(rooftopDto, false))
                        .collect(Collectors.toList())
        );
    }

}
