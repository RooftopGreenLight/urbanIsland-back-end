package rooftopgreenlight.urbanisland.domain.reservation.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import rooftopgreenlight.urbanisland.domain.common.BaseEntity;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopOption;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopPeopleCount;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 추가옵션 별 적용 여부
 * 결제 방식
 * 결제 금액
 */
@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rooftop_option_id")
    private Long id;

    @Column(nullable = false)
    private String content;
    private Integer price;
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Builder(builderMethodName = "createReservation")
    public ReservationOption(String content, Integer price, Integer count) {
        this.content = content;
        this.price = price;
        this.count = count;
    }

    public void changeReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
