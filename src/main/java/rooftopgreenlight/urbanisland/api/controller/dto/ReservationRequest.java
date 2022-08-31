package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import rooftopgreenlight.urbanisland.domain.reservation.entity.PaymentType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReservationRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;

    private int adultCount;
    private int kidCount;
    private int petCount;
    private int totalCount;

    private PaymentType paymentType;

    private String totalPrice;

    private List<String> contents = new ArrayList<>();
    private List<Integer> prices = new ArrayList<>();
    private List<Integer> counts = new ArrayList<>();
}
