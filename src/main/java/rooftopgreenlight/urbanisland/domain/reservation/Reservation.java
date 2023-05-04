package rooftopgreenlight.urbanisland.domain.reservation;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import rooftopgreenlight.urbanisland.domain.model.BaseEntity;
import rooftopgreenlight.urbanisland.domain.member.Member;
import rooftopgreenlight.urbanisland.domain.rooftop.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopPeopleCount;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 추가옵션 별 적용 여부
 * 결제 방식
 * 결제 금액
 */
@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private String tid;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalTime startTime;
    private LocalTime endTime;

    @Embedded
    private RooftopPeopleCount reservationPeopleCount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    public void changeReservationStatus(final ReservationStatus status) {
        this.reservationStatus = status;
    }

    public void changePaymentStatus(final PaymentStatus status) {
        this.paymentStatus = status;
    }

    @Column(length = 30)
    private String totalPrice;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.PERSIST)
    private List<ReservationOption> reservationOptions = new ArrayList<>();

    public void addReservationOption(List<ReservationOption> reservationOptionList) {
        reservationOptionList.forEach(reservationOption -> {
            reservationOptions.add(reservationOption);
            reservationOption.changeReservation(this);
        });
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void changeMember(Member member) {
        this.member = member;
    }

    @BatchSize(size = 30)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rooftop_id")
    private Rooftop rooftop;

    public void changeRooftop(Rooftop rooftop) {
        this.rooftop = rooftop;
    }

    @Builder(builderMethodName = "createReservation")
    public Reservation(String tid, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
                       RooftopPeopleCount reservationPeopleCount, PaymentType paymentType, String totalPrice) {
        this.tid = tid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationPeopleCount = reservationPeopleCount;
        this.paymentType = paymentType;
        this.totalPrice = totalPrice;
        this.paymentStatus = PaymentStatus.PAYMENT_PROGRESSING;
        this.reservationStatus = ReservationStatus.WAITING;
    }
}
