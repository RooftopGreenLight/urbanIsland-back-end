package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.entity.GreenBeeImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;

@Data
public class GreenBeeImageResponse {

    private String type;
    private String uploadFilename;
    private String storeFilename;
    private String fileUrl;
    private ImageType greenBeeImageType;

    private GreenBeeImageResponse(String type, String uploadFilename, String storeFilename, String fileUrl, ImageType greenBeeImageType) {
        this.type = type;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
        this.fileUrl = fileUrl;
        this.greenBeeImageType = greenBeeImageType;
    }

    public static GreenBeeImageResponse of(GreenBeeImage greenBeeImage) {
        return new GreenBeeImageResponse(
                greenBeeImage.getType(),
                greenBeeImage.getUploadFilename(),
                greenBeeImage.getStoreFilename(),
                greenBeeImage.getFileUrl(),
                greenBeeImage.getGreenBeeImageType()
        );
    }
}
