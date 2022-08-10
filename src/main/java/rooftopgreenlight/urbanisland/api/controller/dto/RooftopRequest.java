package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RooftopRequest {

    @NotBlank(message = "올바른 넓이 입력 형식이 아닙니다.")
    private String width;
    private String phoneNumber;
    private String explainContent;
    private String refundContent;
    private String roleContent;
    private String ownerContent;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    private int totalPrice;
    private int widthPrice;

    private int adultCount;
    private int kidCount;
    private int petCount;
    private int totalCount;

    @NotBlank(message = "올바른 도/광역시 입력 형식이 아닙니다.")
    private String county;
    @NotBlank(message = "올바른 시/군/구 입력 형식이 아닙니다.")
    private String city;
    @NotBlank(message = "올바른 세부 주소 입력 형식이 아닙니다.")
    private String detail;

}
