package rooftopgreenlight.urbanisland.dto.owner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;
import rooftopgreenlight.urbanisland.domain.file.OwnerImage;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OwnerInfoResponse {
    private Long id;
    private Long memberId;

    // 이미지 응답
    private OwnerImageResponse confirmationImage;

    private OwnerInfoResponse(Long memberId, OwnerImage confirmationImage) {
        this.memberId = memberId;
        this.confirmationImage = OwnerImageResponse.of(confirmationImage);
    }

    public static OwnerInfoResponse of(Long memberId, OwnerImage confirmationImage) {
        return new OwnerInfoResponse(memberId, confirmationImage);
    }

    public static OwnerInfoResponse of(OwnerDto ownerDto) {
        return new OwnerInfoResponse(ownerDto.getMemberId(), ownerDto.getConfirmationImage());
    }

    public static List<OwnerInfoResponse> of(Page<OwnerDto> ownerDtoPage) {
        return ownerDtoPage.stream().map(OwnerInfoResponse::of).collect(Collectors.toList());
    }
}
