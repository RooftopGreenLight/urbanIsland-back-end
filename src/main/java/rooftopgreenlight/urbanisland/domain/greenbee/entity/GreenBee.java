package rooftopgreenlight.urbanisland.domain.greenbee.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.common.BaseEntity;
import rooftopgreenlight.urbanisland.domain.file.entity.GreenBeeImage;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "member_id", columnList = "member_id")
})
public class GreenBee extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "greenbee_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String officeNumber;
    @Column(nullable = false)
    private String content;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "city", column = @Column(name = "rooftop_address_city")),
                    @AttributeOverride(name = "district", column = @Column(name = "rooftop_address_street")),
                    @AttributeOverride(name = "detail", column = @Column(name = "rooftop_address_detail"))
            }
    )
    private Address address;

    @OneToMany(mappedBy = "greenBee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GreenBeeImage> greenBeeImages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void changeMember(Member member) {
        this.member = member;
    }

    public void addGreenBeeImage(GreenBeeImage greenBeeImage) {
        this.greenBeeImages.add(greenBeeImage);
    }

    @Builder(builderMethodName = "createGreenBee")
    public GreenBee(String officeNumber, String content, Address address) {
        this.officeNumber = officeNumber;
        this.content = content;
        this.address = address;
    }
}
