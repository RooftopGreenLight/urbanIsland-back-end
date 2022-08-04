package rooftopgreenlight.urbanisland.domain.file.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Builder(builderMethodName = "createProfile")
    public Profile(String type, String uploadFilename, String storeFilename, String fileUrl) {
        super(type, uploadFilename, storeFilename, fileUrl);
    }
}
