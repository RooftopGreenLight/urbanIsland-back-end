package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.Data;
import lombok.ToString;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;

@Data
@ToString
public class RooftopImageDto {
    private String type;
    private String uploadFilename;
    private String storeFilename;
    private String fileUrl;
    private ImageType rooftopImageType;

    private RooftopImageDto(String type, String uploadFilename, String storeFilename, String fileUrl, ImageType rooftopImageType) {
        this.type = type;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
        this.fileUrl = fileUrl;
        this.rooftopImageType = rooftopImageType;
    }

    public static RooftopImageDto of(RooftopImage rooftopImage) {
        if (rooftopImage == null) return null;

        return new RooftopImageDto(
                rooftopImage.getType(),
                rooftopImage.getUploadFilename(),
                rooftopImage.getStoreFilename(),
                rooftopImage.getFileUrl(),
                rooftopImage.getRooftopImageType()
        );
    }
}
