package rooftopgreenlight.urbanisland.domain.file.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopImage extends File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rooftop_image_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RooftopImageType rooftopImageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rooftop_id")
    private Rooftop rooftop;

    public void changeRooftop(Rooftop rooftop) {
        this.rooftop = rooftop;
    }

    @Builder(builderMethodName = "createRooftopImage")
    public RooftopImage(String type, String uploadFilename, String storeFilename,
                        String fileUrl, RooftopImageType rooftopImageType) {
        super(type, uploadFilename, storeFilename, fileUrl);
        this.rooftopImageType = rooftopImageType;
    }
}
