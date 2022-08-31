package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.ReservationRequest;
import rooftopgreenlight.urbanisland.domain.reservation.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{rooftopId}")
    @ApiOperation(value = "Reservation 저장",
            notes = "요청 데이터(path) -> key(body) : tid, startDate, endDate, startTime, endTime, adultCount, kidCount, petCount, totalCount" +
                    "paymentType(현재는 'KAKAO_PAY' 고정), totalPrice, contents(list), prices(list), counts(list)")
    public APIResponse createReservation(
            @PK Long memberId,
            @RequestBody ReservationRequest request,
            @PathVariable("rooftopId") Long rooftopId
    ) {
        reservationService.create(memberId, rooftopId, request.getTid(), request.getStartDate(), request.getEndDate(), request.getStartTime(), request.getEndTime(),
                request.getAdultCount(), request.getKidCount(), request.getPetCount(), request.getTotalCount(), request.getPaymentType(),
                request.getTotalPrice(), request.getContents(), request.getPrices(), request.getCounts());

        return APIResponse.createEmpty();
    }
}
