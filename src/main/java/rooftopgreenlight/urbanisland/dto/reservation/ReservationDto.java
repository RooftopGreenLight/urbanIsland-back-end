package rooftopgreenlight.urbanisland.dto.reservation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationDto {
    private Long id;

    private Long ownerId;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer adultCount;

    private Integer kidCount;

    private Integer petCount;

    private String city;

    private String district;

    private String detail;

    private Long rooftopId;

    public static ReservationDto of (Long id, Long ownerId, LocalDate startDate, LocalDate endDate,
                                     LocalTime startTime, LocalTime endTime, Integer adultCount, Integer kidCount, Integer petCount,
                                     String city, String district, String detail, Long rooftopId) {
        return new ReservationDto(id, ownerId, startDate, endDate, startTime, endTime, adultCount, kidCount, petCount, city, district, detail, rooftopId);
    }
}
