package rooftopgreenlight.urbanisland.domain.rooftop.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Double width;
    private Double grade;
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
    private Integer deadLineType;

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

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "rooftop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RooftopGreeningApply> rooftopGreeningApplies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void changeMember(Member member) {
        this.member = member;
    }

    public void changeProgress(Progress progress) {
        this.rooftopProgress = progress;
    }

    public void changeRooftopType(RooftopType rooftopType) {
        this.rooftopType = rooftopType;
    }

    @Builder(builderMethodName = "createRooftop")
    public Rooftop(Double width, String phoneNumber, String explainContent, String refundContent, String roleContent,
                   String ownerContent, LocalTime startTime, LocalTime endTime, Integer totalPrice,
                   Integer widthPrice, RooftopPeopleCount peopleCount, Address address, Integer views, Integer deadLineType) {
        this.width = width;
        this.phoneNumber = phoneNumber;
        if(explainContent != null) this.explainContent = explainContent;
        if(refundContent != null) this.refundContent = refundContent;
        if(roleContent != null) this.roleContent = roleContent;
        if(ownerContent != null) this.ownerContent = ownerContent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        if(widthPrice != null) this.widthPrice = widthPrice;
        this.peopleCount = peopleCount;
        this.address = address;
        this.views = views;
        if(deadLineType != null) this.deadLineType = deadLineType;
        this.grade = 0.0;
    }
}
