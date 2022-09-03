package rooftopgreenlight.urbanisland.domain.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.QRooftop;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static rooftopgreenlight.urbanisland.domain.reservation.entity.QReservation.reservation;
import static rooftopgreenlight.urbanisland.domain.rooftop.entity.QRooftop.rooftop;


public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    private final JPAQueryFactory factory;

    public ReservationRepositoryImpl(EntityManager em) {
        this.factory = new JPAQueryFactory(em);
    }

    @Override
    public List<Reservation> getMyReservationByDate(Long memberId, LocalDate date) {
        return factory.select(reservation)
                .from(reservation)
                .where(reservation.member.id.eq(memberId),
                        reservation.startDate.loe(date).and(reservation.endDate.goe(date)))
                .fetch();
    }
}
