package rooftopgreenlight.urbanisland.dto.owner;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.OwnerImage;
import rooftopgreenlight.urbanisland.domain.file.ImageType;

@Data
public class OwnerImageResponse {

    private String type;
    private String uploadFilename;
    private String storeFilename;
    private String fileUrl;
    private ImageType ownerImageType;

    private OwnerImageResponse(String type, String uploadFilename, String storeFilename, String fileUrl, ImageType ownerImageType) {
        this.type = type;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
        this.fileUrl = fileUrl;
        this.ownerImageType = ownerImageType;
    }

    public static OwnerImageResponse of(OwnerImage ownerImage) {
        if (ownerImage == null) return null;

        return new OwnerImageResponse(
                ownerImage.getType(),
                ownerImage.getUploadFilename(),
                ownerImage.getStoreFilename(),
                ownerImage.getFileUrl(),
                ownerImage.getOwnerImageType()
        );
    }
}
