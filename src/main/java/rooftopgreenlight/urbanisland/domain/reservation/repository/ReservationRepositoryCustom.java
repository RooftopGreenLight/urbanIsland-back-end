package rooftopgreenlight.urbanisland.domain.reservation.repository;

import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> getMyReservationByDate(Long memberId, LocalDate date);
}
