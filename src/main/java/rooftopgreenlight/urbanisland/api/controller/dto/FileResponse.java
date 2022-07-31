package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import rooftopgreenlight.urbanisland.domain.file.entity.Profile;

@Data
@AllArgsConstructor(staticName = "of")
public class FileResponse {
    private Long memberId;
    private String type;
    private String fileUrl;

    public static FileResponse fromProfile(Long memberId, Profile profile) {
        return FileResponse.of(
                memberId,
                profile.getType(),
                profile.getFileUrl()
        );
    }
}
