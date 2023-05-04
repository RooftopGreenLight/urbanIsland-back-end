package rooftopgreenlight.urbanisland.domain.reservation;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

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
