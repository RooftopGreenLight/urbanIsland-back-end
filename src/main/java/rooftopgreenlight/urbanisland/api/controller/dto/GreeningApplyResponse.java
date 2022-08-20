package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.GreeningApplyDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GreeningApplyResponse {
    private Long rooftopId;
    private String rooftopCity;
    private String rooftopDistrict;
    private String rooftopDetail;

    private Long greenBeeId;
    private String officeCity;
    private String officeDistrict;
    private String officeDetail;
    private String officeNumber;
    private String ownerPhoneNumber;

    private LocalDateTime applyTime;
    private Progress progress;

    protected GreeningApplyResponse(Long rooftopId, String rooftopCity, String rooftopDistrict, String rooftopDetail, Long greenBeeId, String officeCity, String officeDistrict, String officeDetail, String officeNumber, LocalDateTime applyTime) {
        this.rooftopId = rooftopId;
        this.rooftopCity = rooftopCity;
        this.rooftopDistrict = rooftopDistrict;
        this.rooftopDetail = rooftopDetail;
        this.greenBeeId = greenBeeId;
        this.officeCity = officeCity;
        this.officeDistrict = officeDistrict;
        this.officeDetail = officeDetail;
        this.officeNumber = officeNumber;
        this.applyTime = applyTime;
    }

    protected GreeningApplyResponse(Long rooftopId, String rooftopCity, String rooftopDistrict, String rooftopDetail, String ownerPhoneNumber) {
        this.rooftopId = rooftopId;
        this.rooftopCity = rooftopCity;
        this.rooftopDistrict = rooftopDistrict;
        this.rooftopDetail = rooftopDetail;
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    protected GreeningApplyResponse(Long rooftopId, String rooftopCity, String rooftopDistrict, String rooftopDetail, Progress progress) {
        this.rooftopId = rooftopId;
        this.rooftopCity = rooftopCity;
        this.rooftopDistrict = rooftopDistrict;
        this.rooftopDetail = rooftopDetail;
        this.progress = progress;
    }

    public static List<GreeningApplyResponse> of(List<GreeningApplyDto> accepted, String type) {
        List<GreeningApplyResponse> responseDtos = new ArrayList<>();

        if(type.equals("SELECTED")) {
            accepted.forEach(accept -> {
                GreeningApplyResponse greeningApplyResponse = new GreeningApplyResponse(accept.getRooftopId(), accept.getRooftopCity(), accept.getRooftopDistrict(),
                        accept.getRooftopDetail(), accept.getProgress());
                responseDtos.add(greeningApplyResponse);
            });
        }

        else {
            accepted.forEach(accept -> {
                GreeningApplyResponse greeningApplyResponse = new GreeningApplyResponse(accept.getRooftopId(), accept.getRooftopCity(), accept.getRooftopDistrict(),
                        accept.getRooftopDetail(), accept.getOwnerPhoneNumber());
                responseDtos.add(greeningApplyResponse);
            });
        }
        return responseDtos;
    }

    public static GreeningApplyResponse of(GreeningApplyDto greeningApply) {
        return new GreeningApplyResponse(greeningApply.getRooftopId(), greeningApply.getRooftopCity(), greeningApply.getRooftopDistrict(),
                greeningApply.getRooftopDistrict(), greeningApply.getGreenBeeId(), greeningApply.getOfficeCity(), greeningApply.getOfficeDistrict(),
                greeningApply.getOfficeDetail(), greeningApply.getOfficeNumber(), greeningApply.getApplyTime());
    }
}
