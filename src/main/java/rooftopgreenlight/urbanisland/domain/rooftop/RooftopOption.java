package rooftopgreenlight.urbanisland.domain.rooftop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rooftop_option_id")
    private Long id;

    @Column(nullable = false)
    private String content;
    private Integer price;
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rooftop_id")
    private Rooftop rooftop;

    public void changeRooftop(Rooftop rooftop) {
        this.rooftop = rooftop;
    }

    protected RooftopOption(String content, Integer price, Integer count) {
        this.content = content;
        this.price = price;
        this.count = count;
    }

    public static RooftopOption of(String content, Integer price, Integer count) {
        return new RooftopOption(content, price, count);
    }
}
