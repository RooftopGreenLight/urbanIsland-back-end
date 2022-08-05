package rooftopgreenlight.urbanisland.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import rooftopgreenlight.urbanisland.domain.common.BaseEntity;
import rooftopgreenlight.urbanisland.domain.file.entity.Profile;

import javax.persistence.*;

@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(updatable = false, nullable = false, length = 10)
    private String name;
    @Column
    private String phoneNumber;
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public void changeProfile(Profile profile) {
        this.profile = profile;
    }
    public void changePassword(String password) {
        this.password = password;
    }
    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Builder(builderMethodName = "createMember")
    public Member(String email, String password, String name, String phoneNumber, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
    }
}
