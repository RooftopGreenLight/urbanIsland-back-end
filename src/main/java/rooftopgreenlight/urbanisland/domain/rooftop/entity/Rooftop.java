package rooftopgreenlight.urbanisland.domain.rooftop.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.common.BaseEntity;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private String explainContent;
    private String refundContent;
    private String roleContent;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private RooftopType rooftopType;

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

    @OneToMany(mappedBy = "rooftop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RooftopImage> rooftopImages = new ArrayList<>();

    @OneToMany(mappedBy = "rooftop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RooftopDetail> rooftopDetails = new ArrayList<>();

    @OneToMany(mappedBy = "rooftop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RooftopOption> rooftopOptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void changeMember(Member member) {
        this.member = member;
    }

    @Builder(builderMethodName = "createRooftop")
    public Rooftop(String width, String explainContent, String refundContent, String roleContent,
                   LocalDateTime startTime, LocalDateTime endTime, RooftopPeopleCount peopleCount,
                   Address address, RooftopType rooftopType) {
        this.width = width;
        this.explainContent = explainContent;
        this.refundContent = refundContent;
        this.roleContent = roleContent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.peopleCount = peopleCount;
        this.address = address;
        this.rooftopType = rooftopType;
    }
}
