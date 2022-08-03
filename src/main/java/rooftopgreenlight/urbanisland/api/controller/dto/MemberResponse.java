package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.entity.Profile;
import rooftopgreenlight.urbanisland.domain.member.entity.Authority;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberResponse {
    private Long id;

    private String email;
    private String name;
    private String phoneNumber;
    private Authority authority;

    private String profileUrl;
    private String profileType;

    protected MemberResponse(long id, String email, String name, String phoneNumber, Authority authority) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
    }

    protected MemberResponse(long id, String email, String name, String phoneNumber, Authority authority, String profileUrl, String profileType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
        this.profileUrl = profileUrl;
        this.profileType = profileType;
    }

    public static MemberResponse of(long id, String email, String name, String phoneNumber, Authority authority) {
        return new MemberResponse(id, email, name, phoneNumber, authority);
    }

    public static MemberResponse of(long id, String email, String name, String phoneNumber, Authority authority, String profileUrl, String profileType) {
        return new MemberResponse(id, email, name, phoneNumber, authority, profileUrl, profileType);
    }

    public static MemberResponse of(Member member) {
        Profile memberProfile = member.getProfile();
        if (memberProfile == null) {
            return MemberResponse.of(member.getId(), member.getEmail(), member.getName(), member.getPhoneNumber(), member.getAuthority());
        }

        return MemberResponse.of(member.getId(), member.getEmail(), member.getName(), member.getPhoneNumber(),
                member.getAuthority(), memberProfile.getFileUrl(), memberProfile.getType());
    }

}
