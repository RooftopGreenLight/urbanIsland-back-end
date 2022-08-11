package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopImageDto;

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
        return new RooftopImageResponse(
                rooftopImage.getType(),
                rooftopImage.getUploadFilename(),
                rooftopImage.getStoreFilename(),
                rooftopImage.getFileUrl(),
                rooftopImage.getRooftopImageType()
        );
    }

}
