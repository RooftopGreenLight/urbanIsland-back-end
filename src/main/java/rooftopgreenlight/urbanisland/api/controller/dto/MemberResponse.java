package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import rooftopgreenlight.urbanisland.api.common.jwt.dto.TokenDto;
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
    private String authority;

    private String profileUrl;
    private String profileType;

    private TokenDto tokenDto;

    protected MemberResponse(long id, String email, String name, String phoneNumber, String authority) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
    }

    protected MemberResponse(long id, String email, String name, String phoneNumber, String authority, String profileUrl, String profileType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
        this.profileUrl = profileUrl;
        this.profileType = profileType;
    }

    protected MemberResponse(Long id, String authority, TokenDto tokenDto) {
        this.id = id;
        this.authority = authority;
        this.tokenDto = tokenDto;
    }

    protected MemberResponse(Long id, TokenDto tokenDto) {
        this.id = id;
        this.tokenDto = tokenDto;
    }

    public static MemberResponse of(long id, String email, String name, String phoneNumber, String authority) {
        return new MemberResponse(id, email, name, phoneNumber, authority);
    }

    public static MemberResponse of(long id, String email, String name, String phoneNumber, String authority, String profileUrl, String profileType) {
        return new MemberResponse(id, email, name, phoneNumber, authority, profileUrl, profileType);
    }

    public static MemberResponse of(Member member) {
        Profile memberProfile = member.getProfile();
        if (memberProfile == null) {
            return MemberResponse.of(member.getId(), member.getEmail(), member.getNickname(),
                    member.getPhoneNumber(), member.getAuthority().toString());
        }

        return MemberResponse.of(member.getId(), member.getEmail(), member.getNickname(), member.getPhoneNumber(),
                member.getAuthority().toString(), memberProfile.getFileUrl(), memberProfile.getType());
    }

    public static MemberResponse of(long id, String authority, TokenDto tokenDto) {
        return new MemberResponse(id, authority, tokenDto);
    }

    public static MemberResponse of(long id, TokenDto tokenDto) {
        return new MemberResponse(id, tokenDto);
    }
}
