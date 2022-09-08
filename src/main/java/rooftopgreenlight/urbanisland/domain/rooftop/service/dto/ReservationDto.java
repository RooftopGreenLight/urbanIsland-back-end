package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by YC on 2022/09/08.
 */
@Data
public class ReservationDto {

    private LocalDate startDates;
    private LocalDate endDates;

    private LocalTime startTimes;
    private LocalTime endTimes;

    protected ReservationDto(LocalDate startDates, LocalDate endDates, LocalTime startTimes, LocalTime endTimes) {
        this.startDates = startDates;
        this.endDates = endDates;
        this.startTimes = startTimes;
        this.endTimes = endTimes;
    }

    static public ReservationDto from(Reservation reservation) {
        return new ReservationDto(reservation.getStartDate(), reservation.getEndDate(),
                reservation.getStartTime(), reservation.getEndTime());
    }
}
