package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.GreenBee;

import java.util.List;

@Data
public class GreenBeeInfoResponse {
    private Long id;
    private Long memberId;
    private String officeNumber;
    private String content;
    private String addressCity;
    private String addressDistrict;
    private String addressDetail;

    private List<GreenBeeImageResponse> greenBeeImages;

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

    public static GreenBeeInfoResponse of(GreenBee greenBee, List<GreenBeeImageResponse> imageResponses) {
        return new GreenBeeInfoResponse(greenBee.getId(), greenBee.getMember().getId(), greenBee.getOfficeNumber(),
                greenBee.getContent(), greenBee.getAddress(), imageResponses);
    }
}
