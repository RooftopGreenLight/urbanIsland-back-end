package rooftopgreenlight.urbanisland.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.common.exception.NotFoundReservationException;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;
import rooftopgreenlight.urbanisland.domain.reservation.entity.PaymentStatus;
import rooftopgreenlight.urbanisland.domain.reservation.entity.PaymentType;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;
import rooftopgreenlight.urbanisland.domain.reservation.entity.ReservationOption;
import rooftopgreenlight.urbanisland.domain.reservation.repository.ReservationRepository;
import rooftopgreenlight.urbanisland.domain.reservation.service.dto.ReservationDto;
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
    public Long create(Long memberId, Long rooftopId, String tid, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
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

        return reservationRepository.save(reservation).getId();
    }

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> {
            throw new NotFoundReservationException("예약 정보를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteOptionsById(reservationId);
        reservationRepository.deleteReservationById(reservationId);
    }

    @Transactional
    public void changeReservationStatus(long reservationId, PaymentStatus status) {
        Reservation reservation = findById(reservationId);

        reservation.changePaymentStatus(status);
    }

    /**
     * 내 예약 정보 조회
     */
    public ReservationDto getMyReservation(Long memberId, LocalDate date) {
        List<Reservation> myReservation = reservationRepository.getMyReservationByDate(memberId, date);

        System.out.println("myReservation.size() = " + myReservation.size());

        if (myReservation.size() !=0) {
            Reservation reservation = myReservation.get(0);
            Rooftop rooftop = reservation.getRooftop();
            Address address = rooftop.getAddress();
            RooftopPeopleCount peopleCount = reservation.getReservationPeopleCount();

            return ReservationDto.of(reservation.getId(), Long.valueOf(rooftop.getCreatedBy()), reservation.getStartDate(), reservation.getEndDate(),
                    reservation.getStartTime(), reservation.getEndTime(), peopleCount.getAdultCount(), peopleCount.getKidCount(),
                    peopleCount.getPetCount(), address.getCity(), address.getDistrict(), address.getDetail());
        }
        return null;
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
                .reservationPeopleCount(RooftopPeopleCount.of(adultCount, kidCount, petCount, totalCount))
                .paymentType(paymentType)
                .totalPrice(totalPrice)
                .build();
    }
}
