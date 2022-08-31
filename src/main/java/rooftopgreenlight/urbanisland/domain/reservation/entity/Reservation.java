package rooftopgreenlight.urbanisland.domain.reservation.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import rooftopgreenlight.urbanisland.domain.common.BaseEntity;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopPeopleCount;

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
    private RooftopPeopleCount rooftopPeopleCount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(length = 30)
    private String totalPrice;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ReservationOption> reservationOptions = new ArrayList<>();

    public void addReservationOption(List<ReservationOption> reservationOptionList) {
        reservationOptionList.forEach(reservationOption -> {
            reservationOptions.add(reservationOption);
            reservationOption.changeReservation(this);
        });
    }

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void changeMember(Member member) {
        this.member = member;
    }

    @ManyToOne
    @JoinColumn(name = "rooftop_id")
    private Rooftop rooftop;

    public void changeRooftop(Rooftop rooftop) {
        this.rooftop = rooftop;
    }

    @Builder(builderMethodName = "createReservation")
    public Reservation(String tid, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
                       RooftopPeopleCount rooftopPeopleCount, PaymentType paymentType, String totalPrice) {
        this.tid = tid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rooftopPeopleCount = rooftopPeopleCount;
        this.paymentType = paymentType;
        this.totalPrice = totalPrice;
        this.paymentStatus = PaymentStatus.PAYMENT_COMPLETED;
    }
}
