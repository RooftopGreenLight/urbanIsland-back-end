package rooftopgreenlight.urbanisland.domain.owner;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.model.BaseEntity;
import rooftopgreenlight.urbanisland.domain.model.Progress;
import rooftopgreenlight.urbanisland.domain.file.OwnerImage;
import rooftopgreenlight.urbanisland.domain.member.Member;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "rooftop_owner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "owner_image_id")
    private OwnerImage ownerImage;

    public void changeMember(Member member) {
        this.member = member;
    }

    public void changeProgress(Progress progress) {
        this.progress = progress;
    }
    
    @Builder(builderMethodName = "createOwner")
    public Owner(OwnerImage ownerImage) {
        this.ownerImage = ownerImage;
    }
}
