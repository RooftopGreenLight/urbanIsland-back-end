package rooftopgreenlight.urbanisland.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;
import rooftopgreenlight.urbanisland.domain.reservation.entity.PaymentType;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;
import rooftopgreenlight.urbanisland.domain.reservation.entity.ReservationOption;
import rooftopgreenlight.urbanisland.domain.reservation.repository.ReservationRepository;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopPeopleCount;
import rooftopgreenlight.urbanisland.domain.rooftop.service.RooftopService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final MemberService memberService;
    private final RooftopService rooftopService;

    @Transactional
    public void create(Long memberId, Long rooftopId, String tid, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
                       int adultCount, int kidCount, int petCount, int totalCount, PaymentType paymentType, String totalPrice,
                       List<String> contents, List<Integer> prices, List<Integer> counts) {

        Member member = memberService.findById(memberId);
        Rooftop rooftop = rooftopService.findByRooftopId(rooftopId);

        Reservation reservation = createReservation(tid, startDate, endDate, startTime, endTime, adultCount, kidCount, petCount, totalCount, paymentType, totalPrice);
        reservation.changeMember(member);
        reservation.changeRooftop(rooftop);

        ArrayList<ReservationOption> reservationOptions = new ArrayList<>();
        if (contents.size() != 0) {
            for (int i = 0; i < contents.size(); i++) {
                ReservationOption reservationOption = ReservationOption.createReservation()
                        .content(contents.get(i))
                        .price(prices.get(i))
                        .count(counts.get(i))
                        .build();

                reservationOptions.add(reservationOption);
            }
        }

        reservation.addReservationOption(reservationOptions);

        reservationRepository.save(reservation);
    }

    private static Reservation createReservation(String tid,LocalDate startDate, LocalDate endDate,
                                                 LocalTime startTime, LocalTime endTime, int adultCount, int kidCount,
                                                 int petCount, int totalCount, PaymentType paymentType,
                                                 String totalPrice) {
        return Reservation.createReservation()
                .tid(tid)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .rooftopPeopleCount(RooftopPeopleCount.of(adultCount, kidCount, petCount, totalCount))
                .paymentType(paymentType)
                .totalPrice(totalPrice)
                .build();
    }
}
