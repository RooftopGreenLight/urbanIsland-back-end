package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.file.entity.GreenBeeImage;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.GreenBee;
import rooftopgreenlight.urbanisland.domain.greenbee.service.dto.GreenBeeDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GreenBeeInfoResponse {
    private Long id;
    private Long memberId;
    private String officeNumber;
    private String content;
    private String addressCity;
    private String addressDistrict;
    private String addressDetail;

    // 이미지 응답
    private List<GreenBeeImageResponse> greenBeeImages;
    private GreenBeeImageResponse confirmationImage;

    private GreenBeeInfoResponse(Long id, Long memberId, String officeNumber, String content, Address address, List<GreenBeeImageResponse> greenBeeImages) {
        this.id = id;
        this.memberId = memberId;
        this.officeNumber = officeNumber;
        this.content = content;
        this.addressCity = address.getCity();
        this.addressDistrict = address.getDistrict();
        this.addressDetail = address.getDetail();
        this.greenBeeImages = greenBeeImages;
    }

    private GreenBeeInfoResponse(Long memberId, String officeNumber,
                       String content, GreenBeeImage confirmationImage) {

        this.memberId = memberId;
        this.officeNumber = officeNumber;
        this.content = content;
        this.confirmationImage = GreenBeeImageResponse.of(confirmationImage);
    }

    public static GreenBeeInfoResponse of(GreenBee greenBee, List<GreenBeeImageResponse> imageResponses) {
        return new GreenBeeInfoResponse(greenBee.getId(), greenBee.getMember().getId(), greenBee.getOfficeNumber(),
                greenBee.getContent(), greenBee.getAddress(), imageResponses);
    }

    public static GreenBeeInfoResponse of(GreenBeeDto greenBeeDto) {
        return new GreenBeeInfoResponse(greenBeeDto.getMemberId(), greenBeeDto.getOfficeNumber(),
                greenBeeDto.getContent(), greenBeeDto.getConfirmationImage());
    }



    public static List<GreenBeeInfoResponse> of(Page<GreenBeeDto> greenBeesPage) {
        List<GreenBeeDto> content = greenBeesPage.getContent();

        return content.stream().map(GreenBeeInfoResponse::of).collect(Collectors.toList());
    }

}
