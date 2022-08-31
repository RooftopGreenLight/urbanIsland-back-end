package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopOptionDto;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopOptionResponse {
    private Long id;
    private String content;
    private Integer price;
    private Integer count;

    public static RooftopOptionResponse of(RooftopOptionDto optionDto) {
        return new RooftopOptionResponse(optionDto.getId(), optionDto.getContent(), optionDto.getPrice(), optionDto.getCount());
    }
}
