package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.reservation.service.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private Integer adultCount;

    private Integer kidCount;

    private Integer petCount;

    private String city;

    private String district;

    private String detail;

    private Long rooftopId;

    protected ReservationResponse(Long reservationId, Long ownerId, LocalDate startDate, LocalDate endDate,
                                  LocalTime startTime, LocalTime endTime, Integer adultCount, Integer kidCount,
                                  Integer petCount, String city, String district, String detail, Long rooftopId) {
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
        this.rooftopId = rooftopId;
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

    public List<ReservationResponse> fromReservationDtoList(List<ReservationDto> reservationDtos) {
        if (reservationDtos == null) return null;

        return reservationDtos.stream().map(
            reservationDto -> new ReservationResponse(
                    reservationDto.getId(), null,
                    reservationDto.getStartDate(), reservationDto.getEndDate(), reservationDto.getStartTime(), reservationDto.getEndTime(),
                    null, null, null,
                    reservationDto.getCity(), reservationDto.getDistrict(), reservationDto.getDetail(), reservationDto.getRooftopId()
            )).collect(Collectors.toList());
    }

    public static ReservationResponse of(ReservationDto reservationDto) {
        if (reservationDto != null) {
            return new ReservationResponse(reservationDto.getId(), reservationDto.getOwnerId(), reservationDto.getStartDate(), reservationDto.getEndDate(),
                    reservationDto.getStartTime(), reservationDto.getEndTime(), reservationDto.getAdultCount(), reservationDto.getKidCount(),
                    reservationDto.getPetCount(), reservationDto.getCity(), reservationDto.getDistrict(), reservationDto.getDetail(), reservationDto.getRooftopId());
        }
        return null;
    }

}
