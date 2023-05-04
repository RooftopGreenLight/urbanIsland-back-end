package rooftopgreenlight.urbanisland.dto.rooftop;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopOptionDto {
    private Long id;
    private String content;
    private Integer price;
    private Integer count;

    public static RooftopOptionDto of(Long id, String content, Integer price, Integer count) {
        return new RooftopOptionDto(id, content, price, count);
    }
}
