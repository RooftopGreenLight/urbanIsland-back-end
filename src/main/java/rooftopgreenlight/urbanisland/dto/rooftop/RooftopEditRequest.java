package rooftopgreenlight.urbanisland.dto.rooftop;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class RooftopEditRequest {
    private Integer adultCount;
    private Integer kidCount;
    private Integer petCount;
    private Integer totalCount;

    private Integer totalPrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;
}
