package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.entity.Profile;

@Data
@AllArgsConstructor(staticName = "of")
public class FileResponse {
    private String type;
    private String fileUrl;

    public static FileResponse fromProfile(Profile profile) {
        return FileResponse.of(
                profile.getType(),
                profile.getFileUrl()
        );
    }

}
