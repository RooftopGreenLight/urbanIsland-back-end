package rooftopgreenlight.urbanisland.domain.reservation.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReservationStatus {

    WAITING("대기 중"), COMPLETED("만료");

    private String description;

}
