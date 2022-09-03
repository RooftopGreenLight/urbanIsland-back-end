package rooftopgreenlight.urbanisland.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Modifying
    @Query("delete from ReservationOption ro where ro.reservation.id = :reservationId")
    void deleteOptionsById(@Param("reservationId") long reservationId);

    @Modifying
    @Query("delete from Reservation r where r.id = :reservationId")
    void deleteReservationById(@Param("reservationId") long reservationId);
}
