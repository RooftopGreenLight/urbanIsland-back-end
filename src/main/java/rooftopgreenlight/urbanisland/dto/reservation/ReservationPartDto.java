package rooftopgreenlight.urbanisland.dto.reservation;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.reservation.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by YC on 2022/09/08.
 */
@Data
public class ReservationPartDto {

    private Long id;
    private LocalDate startDates;
    private LocalDate endDates;

    private LocalTime startTimes;
    private LocalTime endTimes;

    protected ReservationPartDto(Long id, LocalDate startDates, LocalDate endDates, LocalTime startTimes, LocalTime endTimes) {
        this.id = id;
        this.startDates = startDates;
        this.endDates = endDates;
        this.startTimes = startTimes;
        this.endTimes = endTimes;
    }

    static public ReservationPartDto from(Reservation reservation) {
        return new ReservationPartDto(reservation.getId(), reservation.getStartDate(), reservation.getEndDate(),
                reservation.getStartTime(), reservation.getEndTime());
    }
}
