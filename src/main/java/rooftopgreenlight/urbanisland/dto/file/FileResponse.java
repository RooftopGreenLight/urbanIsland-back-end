package rooftopgreenlight.urbanisland.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.Profile;

@Data
@AllArgsConstructor(staticName = "of")
public class FileResponse {
    private String type;
    private String fileUrl;

    public static FileResponse fromProfile(Profile profile) {
        if (profile == null) return null;

        return FileResponse.of(
                profile.getType(),
                profile.getFileUrl()
        );
    }

}
