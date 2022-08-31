package rooftopgreenlight.urbanisland.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
