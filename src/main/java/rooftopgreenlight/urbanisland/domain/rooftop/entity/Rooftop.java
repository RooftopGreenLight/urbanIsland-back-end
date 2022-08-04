package rooftopgreenlight.urbanisland.domain.rooftop.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rooftop {

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
    @Column(nullable = false)
    private RooftopAddress rooftopAddress;

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
                   RooftopAddress rooftopAddress, RooftopType rooftopType) {
        this.width = width;
        this.explainContent = explainContent;
        this.refundContent = refundContent;
        this.roleContent = roleContent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.peopleCount = peopleCount;
        this.rooftopAddress = rooftopAddress;
        this.rooftopType = rooftopType;
    }
}
