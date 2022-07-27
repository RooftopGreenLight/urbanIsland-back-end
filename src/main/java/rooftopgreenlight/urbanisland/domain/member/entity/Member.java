package rooftopgreenlight.urbanisland.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.BaseEntity;

import javax.persistence.*;

@Getter
@Entity
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

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
