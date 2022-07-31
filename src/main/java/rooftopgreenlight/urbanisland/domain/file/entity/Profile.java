package rooftopgreenlight.urbanisland.domain.file.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String uploadFilename;
    private String storeFilename;
    private String fileUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void changeProfile(String type, String uploadFilename, String storeFilename, String fileUrl) {
        if (StringUtils.hasText(type)) this.type = type;
        if (StringUtils.hasText(uploadFilename)) this.uploadFilename = uploadFilename;
        if (StringUtils.hasText(storeFilename)) this.storeFilename = storeFilename;
        if (StringUtils.hasText(fileUrl)) this.fileUrl = fileUrl;
    }

    @Builder(builderMethodName = "createProfile")
    public Profile(String type, String uploadFilename, String storeFilename, String fileUrl) {
        this.type = type;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
        this.fileUrl = fileUrl;
    }
}
