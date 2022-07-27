package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

@Data
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberResponse {
    private Long id;

    private String email;
    private String name;
    private String phoneNumber;

    public static MemberResponse of(Member member) {
        return MemberResponse.of(member.getId(), member.getEmail(), member.getName(), member.getPhoneNumber());
    }

    public static MemberResponse emailSend(String email) {
        return MemberResponse.of(null, email, null, null);
    }
}
