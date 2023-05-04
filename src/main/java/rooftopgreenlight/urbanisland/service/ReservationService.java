package rooftopgreenlight.urbanisland.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.domain.model.Address;
import rooftopgreenlight.urbanisland.exception.NotFoundReservationException;
import rooftopgreenlight.urbanisland.domain.member.Member;
import rooftopgreenlight.urbanisland.domain.reservation.PaymentStatus;
import rooftopgreenlight.urbanisland.domain.reservation.PaymentType;
import rooftopgreenlight.urbanisland.domain.reservation.Reservation;
import rooftopgreenlight.urbanisland.domain.reservation.ReservationOption;
import rooftopgreenlight.urbanisland.domain.reservation.ReservationStatus;
import rooftopgreenlight.urbanisland.repository.reservation.ReservationRepository;
import rooftopgreenlight.urbanisland.dto.reservation.ReservationDto;
import rooftopgreenlight.urbanisland.domain.rooftop.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopPeopleCount;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ReservationDto> getMyReservation(Long memberId, LocalDate date) {
        List<Reservation> myReservation = reservationRepository.getMyReservationByDate(memberId, date);

        if (myReservation.size() == 0) {
            return null;
        }

        return myReservation.stream().map(reservation -> {
            Rooftop rooftop = reservation.getRooftop();
            Address address = rooftop.getAddress();
            RooftopPeopleCount peopleCount = reservation.getReservationPeopleCount();

            return ReservationDto.of(reservation.getId(), Long.valueOf(rooftop.getCreatedBy()), reservation.getStartDate(), reservation.getEndDate(),
                    reservation.getStartTime(), reservation.getEndTime(), peopleCount.getAdultCount(), peopleCount.getKidCount(),
                    peopleCount.getPetCount(), address.getCity(), address.getDistrict(), address.getDetail(), rooftop.getId());
        }).collect(Collectors.toList());
    }

    public List<ReservationDto> getMyReservation(Long memberId, ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndReservationStatusAndPaymentStatus(
                memberId,
                status,
                PaymentStatus.PAYMENT_COMPLETED
        );

        return reservations.stream().map(reservation -> ReservationDto.of(
                reservation.getId(),
                null,
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                null,
                null,
                null,
                reservation.getRooftop().getAddress().getCity(),
                reservation.getRooftop().getAddress().getDistrict(),
                reservation.getRooftop().getAddress().getDetail(),
                reservation.getRooftop().getId()
        )).collect(Collectors.toList());
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
