package rooftopgreenlight.urbanisland.domain.file;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;
import rooftopgreenlight.urbanisland.domain.model.BaseEntity;

import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class File extends BaseEntity {

    private String type;
    private String uploadFilename;
    private String storeFilename;
    private String fileUrl;

    public void changeFile(String type, String uploadFilename, String storeFilename, String fileUrl) {
        if (StringUtils.hasText(type)) this.type = type;
        if (StringUtils.hasText(uploadFilename)) this.uploadFilename = uploadFilename;
        if (StringUtils.hasText(storeFilename)) this.storeFilename = storeFilename;
        if (StringUtils.hasText(fileUrl)) this.fileUrl = fileUrl;
    }

    @Builder(builderMethodName = "createFile")
    public File(String type, String uploadFilename, String storeFilename, String fileUrl) {
        this.type = type;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
        this.fileUrl = fileUrl;
    }
}
