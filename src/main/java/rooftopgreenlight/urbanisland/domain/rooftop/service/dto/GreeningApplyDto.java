package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopGreeningApply;

import java.time.LocalDateTime;

@Data
public class GreeningApplyDto {
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

    protected GreeningApplyDto(Long rooftopId, String rooftopCity, String rooftopDistrict, String rooftopDetail, Long greenBeeId, String officeCity, String officeDistrict, String officeDetail, String officeNumber, LocalDateTime applyTime) {
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

    protected GreeningApplyDto(Long rooftopId, String rooftopCity, String rooftopDistrict, String rooftopDetail, String ownerPhoneNumber) {
        this.rooftopId = rooftopId;
        this.rooftopCity = rooftopCity;
        this.rooftopDistrict = rooftopDistrict;
        this.rooftopDetail = rooftopDetail;
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    protected GreeningApplyDto(Long rooftopId, String rooftopCity, String rooftopDistrict, String rooftopDetail, Progress progress) {
        this.rooftopId = rooftopId;
        this.rooftopCity = rooftopCity;
        this.rooftopDistrict = rooftopDistrict;
        this.rooftopDetail = rooftopDetail;
        this.progress = progress;
    }

    public static GreeningApplyDto of(Long rooftopId, String rooftopCity, String rooftopDistrict, String rooftopDetail,
                                      Long greenBeeId, String officeCity, String officeDistrict, String officeDetail,
                                      String officeNumber, LocalDateTime applyTime) {
        return new GreeningApplyDto(rooftopId, rooftopCity, rooftopDistrict, rooftopDetail, greenBeeId, officeCity, officeDistrict, officeDetail, officeNumber, applyTime);
    }

    public static GreeningApplyDto of(RooftopGreeningApply rooftopGreening, String type) {
        return type.equals("SELECTED") ? getSelectedRooftop(rooftopGreening) :getAcceptedRooftop(rooftopGreening);
    }

    private static GreeningApplyDto getAcceptedRooftop(RooftopGreeningApply rooftopGreening) {
        Address address = rooftopGreening.getRooftop().getAddress();
        return new GreeningApplyDto(
                rooftopGreening.getRooftop().getId(), address.getCity(), address.getDistrict(), address.getDetail(),
                rooftopGreening.getRooftop().getMember().getPhoneNumber());
    }

    private static GreeningApplyDto getSelectedRooftop(RooftopGreeningApply rooftopGreening) {
        Address address = rooftopGreening.getRooftop().getAddress();
        return new GreeningApplyDto(
                rooftopGreening.getRooftop().getId(), address.getCity(), address.getDistrict(), address.getDetail(),
                rooftopGreening.getGreeningProgress());
    }


}
