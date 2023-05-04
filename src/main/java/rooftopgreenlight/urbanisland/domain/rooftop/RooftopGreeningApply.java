package rooftopgreenlight.urbanisland.domain.rooftop;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.model.Progress;
import rooftopgreenlight.urbanisland.domain.greenbee.GreenBee;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopGreeningApply {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rooftop_greening_apply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rooftop_id")
    private Rooftop rooftop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "greenbee_id")
    private GreenBee greenBee;

    @Column(name = "member_id")
    private Long greenBeeMemberId;

    private LocalDateTime applyTime;

    @Enumerated(EnumType.STRING)
    private Progress greeningProgress;

    public void changeRooftop(Rooftop rooftop) {
        this.rooftop = rooftop;
    }

    public void changeGreenBee(GreenBee greenBee) {
        this.greenBee = greenBee;
    }

    public void changeProgress(Progress progress) {
        this.greeningProgress = progress;
    }

    @Builder(builderMethodName = "createApply")
    public RooftopGreeningApply(LocalDateTime applyTime, Long greenBeeMemberId) {
        this.applyTime = applyTime;
        this.greenBeeMemberId = greenBeeMemberId;
    }
}
