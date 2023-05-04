package rooftopgreenlight.urbanisland.dto.rooftop;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.ImageType;

@Data
public class RooftopImageResponse {
    private String type;
    private String uploadFilename;
    private String storeFilename;
    private String fileUrl;
    private ImageType rooftopImageType;

    private RooftopImageResponse(String type, String uploadFilename, String storeFilename, String fileUrl, ImageType rooftopImageType) {
        this.type = type;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
        this.fileUrl = fileUrl;
        this.rooftopImageType = rooftopImageType;
    }

    public static RooftopImageResponse of(RooftopImage rooftopImage) {
        return new RooftopImageResponse(
                rooftopImage.getType(),
                rooftopImage.getUploadFilename(),
                rooftopImage.getStoreFilename(),
                rooftopImage.getFileUrl(),
                rooftopImage.getRooftopImageType()
        );
    }

    public static RooftopImageResponse of(RooftopImageDto rooftopImage) {
        if (rooftopImage == null) return null;

        return new RooftopImageResponse(
                rooftopImage.getType(),
                rooftopImage.getUploadFilename(),
                rooftopImage.getStoreFilename(),
                rooftopImage.getFileUrl(),
                rooftopImage.getRooftopImageType()
        );
    }

}
