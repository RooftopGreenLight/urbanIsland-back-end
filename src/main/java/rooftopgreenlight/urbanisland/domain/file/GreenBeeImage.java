package rooftopgreenlight.urbanisland.domain.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.greenbee.GreenBee;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GreenBeeImage extends File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "greenbee_image_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ImageType greenBeeImageType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "greenbee_id")
    private GreenBee greenBee;

    public void changeGreenBee(GreenBee greenBee) {
        this.greenBee = greenBee;
    }

    @Builder(builderMethodName = "createGreenBeeImage")
    public GreenBeeImage(String type, String uploadFilename, String storeFilename, String fileUrl,
                         ImageType greenBeeImageType) {
        super(type, uploadFilename, storeFilename, fileUrl);
        this.greenBeeImageType = greenBeeImageType;
    }
}
