package rooftopgreenlight.urbanisland.domain.greenbee.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.file.entity.GreenBeeImage;

@Data
@NoArgsConstructor
public class GreenBeeDto {

    private Long memberId;
    private String officeNumber;
    private String content;
    private GreenBeeImage confirmationImage;

    /**
     * Admin 페이지에서 승인을 할 때 필요한 데이터
     */
    @QueryProjection
    public GreenBeeDto(Long memberId, String officeNumber,
                                String content, GreenBeeImage confirmationImage) {

        this.memberId = memberId;
        this.officeNumber = officeNumber;
        this.content = content;
        this.confirmationImage = confirmationImage;
    }
}
