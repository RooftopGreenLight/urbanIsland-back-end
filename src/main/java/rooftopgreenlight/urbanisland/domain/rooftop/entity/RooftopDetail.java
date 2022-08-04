package rooftopgreenlight.urbanisland.domain.rooftop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rooftop_detail_id")
    private Long id;

    @Column(nullable = false)
    private int contentNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rooftop_id")
    private Rooftop rooftop;

    public void changeRooftop(Rooftop rooftop) {
        this.rooftop = rooftop;
    }

    protected RooftopDetail(int contentNum) {
        this.contentNum = contentNum;
    }

    public static RooftopDetail of(int contentNum) {
        return new RooftopDetail(contentNum);
    }

}
