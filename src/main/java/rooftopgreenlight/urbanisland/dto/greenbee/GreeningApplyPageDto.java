package rooftopgreenlight.urbanisland.dto.greenbee;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.model.Address;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopGreeningApply;

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
            Address greenBeeAddress = greenApply.getGreenBee().getAddress();

            return GreeningApplyDto.of(greenApply.getRooftop().getId(), greenApply.getGreenBee().getMember().getId(),
                    greenBeeAddress.getCity(), greenBeeAddress.getDistrict(), greenBeeAddress.getDetail(),
                    greenApply.getGreenBee().getOfficeNumber(), greenApply.getApplyTime());
        }).collect(Collectors.toList());

        return new GreeningApplyPageDto(totalPages, totalElements, greeningApplyDtos);
    }
}
