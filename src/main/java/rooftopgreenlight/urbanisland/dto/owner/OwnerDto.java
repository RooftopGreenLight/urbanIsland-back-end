package rooftopgreenlight.urbanisland.dto.owner;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.file.OwnerImage;

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
