package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopGreeningApply;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GreeningApplyPageDto {
    private int totalPages;
    private long totalElements;
    private List<GreeningApplyDto> applyDtos;

    public static GreeningApplyPageDto of (int totalPages, long totalElements, List<RooftopGreeningApply> greeningApplies) {
        List<GreeningApplyDto> greeningApplyDtos = greeningApplies.stream().map(greenApply -> {
            Address rooftopAddress = greenApply.getRooftop().getAddress();
            Address greenBeeAddress = greenApply.getGreenBee().getAddress();

            return GreeningApplyDto.of(greenApply.getRooftop().getId(), rooftopAddress.getCity(),
                    rooftopAddress.getDistrict(), rooftopAddress.getDetail(),
                    greenApply.getGreenBee().getMember().getId(), greenBeeAddress.getCity(), greenBeeAddress.getDistrict(), greenBeeAddress.getDetail(),
                    greenApply.getGreenBee().getOfficeNumber(), LocalDateTime.now());
        }).collect(Collectors.toList());

        return new GreeningApplyPageDto(totalPages, totalElements, greeningApplyDtos);
    }
}
