package rooftopgreenlight.urbanisland.domain.file.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnerImage extends File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_image_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ImageType ownerImageType;

    @Builder(builderMethodName = "createOwnerImage")
    public OwnerImage(String type, String uploadFilename, String storeFilename, String fileUrl,
                      ImageType ownerImageType) {
        super(type, uploadFilename, storeFilename, fileUrl);
        this.ownerImageType = ownerImageType;
    }
}
