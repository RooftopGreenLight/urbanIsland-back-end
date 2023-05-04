package rooftopgreenlight.urbanisland.repository.reservation;

import java.time.LocalDate;
import java.util.List;

import rooftopgreenlight.urbanisland.domain.reservation.Reservation;

public interface ReservationRepositoryCustom {
    List<Reservation> getMyReservationByDate(Long memberId, LocalDate date);
}
