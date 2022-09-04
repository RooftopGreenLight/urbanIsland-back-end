package rooftopgreenlight.urbanisland.domain.reservation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.reservation.entity.PaymentStatus;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;
import rooftopgreenlight.urbanisland.domain.reservation.entity.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {

    @Modifying
    @Query("delete from ReservationOption ro where ro.reservation.id = :reservationId")
    void deleteOptionsById(@Param("reservationId") long reservationId);

    @Modifying
    @Query("delete from Reservation r where r.id = :reservationId")
    void deleteReservationById(@Param("reservationId") long reservationId);

    Page<Reservation> findReservationsByEndDateBeforeAndReservationStatusAndPaymentStatus(LocalDate nowDate,
                                                                          ReservationStatus reservationStatus,
                                                                          PaymentStatus paymentStatus,
                                                                          Pageable pageable);

    List<Reservation> findReservationsByMemberIdAndReservationStatusAndPaymentStatus(long memberId,
                                                                                      ReservationStatus reservationStatus,
                                                                                      PaymentStatus paymentStatus);
}
