package rooftopgreenlight.urbanisland.domain.owner.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.file.entity.GreenBeeImage;
import rooftopgreenlight.urbanisland.domain.file.entity.OwnerImage;
import rooftopgreenlight.urbanisland.domain.owner.entity.Owner;

@Data
@NoArgsConstructor
public class OwnerDto {

    private Long memberId;
    private OwnerImage confirmationImage;

    /**
     * Admin 페이지에서 승인을 할 때 필요한 데이터
     */
    @QueryProjection
    public OwnerDto(Long memberId, OwnerImage confirmationImage) {

        this.memberId = memberId;
        this.confirmationImage = confirmationImage;
    }
}
