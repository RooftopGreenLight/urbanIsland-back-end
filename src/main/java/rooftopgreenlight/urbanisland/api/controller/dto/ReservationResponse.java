package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.reservation.service.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReservationResponse {

    private Long reservationId;

    private String tid;

    private Long ownerId;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private int adultCount;

    private int kidCount;

    private int petCount;

    private String city;

    private String district;

    private String detail;

    protected ReservationResponse(Long reservationId, Long ownerId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int adultCount, int kidCount, int petCount, String city, String district, String detail) {
        this.reservationId = reservationId;
        this.ownerId = ownerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.adultCount = adultCount;
        this.kidCount = kidCount;
        this.petCount = petCount;
        this.city = city;
        this.district = district;
        this.detail = detail;
    }

    protected ReservationResponse(Long reservationId, String tid) {
        this.reservationId = reservationId;
        this.tid = tid;
    }

    public ReservationResponse fromReservationId(Long reservationId) {
        return new ReservationResponse(reservationId, null);
    }

    public ReservationResponse fromTid(String tid) {
        return new ReservationResponse(null, tid);
    }

    public static ReservationResponse of(ReservationDto reservationDto) {
        if (reservationDto != null) {
            return new ReservationResponse(reservationDto.getId(), reservationDto.getOwnerId(), reservationDto.getStartDate(), reservationDto.getEndDate(),
                    reservationDto.getStartTime(), reservationDto.getEndTime(), reservationDto.getAdultCount(), reservationDto.getKidCount(),
                    reservationDto.getPetCount(), reservationDto.getCity(), reservationDto.getDistrict(), reservationDto.getDetail());
        }
        return null;
    }
}
