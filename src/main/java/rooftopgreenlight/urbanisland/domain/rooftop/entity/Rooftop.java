package rooftopgreenlight.urbanisland.domain.rooftop.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.common.BaseEntity;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rooftop extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rooftop_id")
    private Long id;

    @Column(nullable = false)
    private String width;
    private String phoneNumber;

    private String explainContent;
    private String refundContent;
    private String roleContent;
    private String ownerContent;

    @Column(nullable = false)
    private Integer totalPrice;
    private Integer widthPrice;

    @Column(name = "rooftop_views")
    private Integer views;

    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private RooftopType rooftopType;

    @Enumerated(EnumType.STRING)
    private Progress rooftopProgress;

    @Embedded
    @Column(nullable = false)
    private RooftopPeopleCount peopleCount;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "city", column = @Column(name = "rooftop_address_city")),
                    @AttributeOverride(name = "district", column = @Column(name = "rooftop_address_street")),
                    @AttributeOverride(name = "detail", column = @Column(name = "rooftop_address_detail"))
            }
    )
    private Address address;

    @BatchSize(size = 6)
    @OneToMany(mappedBy = "rooftop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RooftopImage> rooftopImages = new ArrayList<>();

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "rooftop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RooftopDetail> rooftopDetails = new ArrayList<>();

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "rooftop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RooftopOption> rooftopOptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void changeMember(Member member) {
        this.member = member;
    }

    @Builder(builderMethodName = "createRooftop")
    public Rooftop(String width, String phoneNumber, String explainContent, String refundContent, String roleContent,
                   String ownerContent, LocalTime startTime, LocalTime endTime, Integer totalPrice,
                   Integer widthPrice, RooftopPeopleCount peopleCount, Address address,
                   RooftopType rooftopType, Progress rooftopProgress, Integer views) {
        this.width = width;
        this.phoneNumber = phoneNumber;
        this.explainContent = explainContent;
        this.refundContent = refundContent;
        this.roleContent = roleContent;
        this.ownerContent = ownerContent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.widthPrice = widthPrice;
        this.peopleCount = peopleCount;
        this.address = address;
        this.rooftopType = rooftopType;
        this.rooftopProgress = rooftopProgress;
        this.views = views;
    }
}
