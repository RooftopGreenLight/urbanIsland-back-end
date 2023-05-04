package rooftopgreenlight.urbanisland.dto.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import rooftopgreenlight.urbanisland.domain.reservation.PaymentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReservationRequest {

    @NotBlank(message = "tid가 빈 값입니다..")
    private String tid;

    @NotNull(message = "rooftopId가 빈 값입니다..")
    private Long rooftopId;

    @NotNull(message = "startDate가 빈 값입니다..")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @NotNull(message = "endDate가 빈 값입니다..")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull(message = "startTime가 빈 값입니다..")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    @NotNull(message = "endTime가 빈 값입니다..")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;

    private int adultCount;
    private int kidCount;
    private int petCount;
    private int totalCount;

    private PaymentType paymentType;

    @NotBlank(message = "totalPrice가 빈 값입니다..")
    private String totalPrice;

    private List<String> contents = new ArrayList<>();
    private List<Integer> prices = new ArrayList<>();
    private List<Integer> counts = new ArrayList<>();
}
