package rooftopgreenlight.urbanisland.dto.rooftop;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class RooftopRequest {

    private Double width;
    private String phoneNumber;
    private String explainContent;
    private String refundContent;
    private String roleContent;
    private String ownerContent;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;

    private int totalPrice;
    private int widthPrice;

    private int adultCount;
    private int kidCount;
    private int petCount;
    private int totalCount;

    @NotBlank(message = "올바른 도/광역시 입력 형식이 아닙니다.")
    private String city;
    @NotBlank(message = "올바른 시/군/구 입력 형식이 아닙니다.")
    private String district;
    @NotBlank(message = "올바른 세부 주소 입력 형식이 아닙니다.")
    private String detail;

}
