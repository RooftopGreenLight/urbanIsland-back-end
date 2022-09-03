package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.ReservationRequest;
import rooftopgreenlight.urbanisland.api.controller.dto.ReservationResponse;
import rooftopgreenlight.urbanisland.domain.reservation.entity.PaymentStatus;
import rooftopgreenlight.urbanisland.domain.reservation.service.ReservationService;

import java.time.LocalDate;
import java.util.Map;

@Api(value = "예약 처리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ApiOperation(value = "Reservation 저장",
            notes = "요청 데이터(body) -> key(body) : tid, rooftopId, startDate, endDate, startTime, endTime, adultCount, kidCount, petCount, totalCount" +
                    "paymentType(현재는 'KAKAO_PAY' 고정), totalPrice, contents(list), prices(list), counts(list)")
    public APIResponse createReservation(
            @PK Long memberId,
            @Validated @RequestBody ReservationRequest request
    ) {
        Long reservationId = reservationService.create(memberId, request.getRooftopId(), request.getTid(), request.getStartDate(), request.getEndDate(), request.getStartTime(), request.getEndTime(),
                request.getAdultCount(), request.getKidCount(), request.getPetCount(), request.getTotalCount(), request.getPaymentType(),
                request.getTotalPrice(), request.getContents(), request.getPrices(), request.getCounts());

        return APIResponse.of(new ReservationResponse().fromReservationId(reservationId));
    }

    @GetMapping("/{reservationId}")
    @ApiOperation(value = "Reservation 조회",
            notes = "요청 데이터(path) -> reservationId")
    public APIResponse getReservation(
            @PathVariable("reservationId") Long reservationId
    ) {
        return APIResponse.of(
                new ReservationResponse().fromTid(reservationService.findById(reservationId).getTid())
        );
    }

    @DeleteMapping("/{reservationId}")
    @ApiOperation(value = "Reservation 삭제",
            notes = "요청 데이터(path) -> reservationId")
    public APIResponse deleteReservation(
            @PathVariable("reservationId") Long reservationId
    ) {
        reservationService.deleteReservation(reservationId);

        return APIResponse.empty();
    }

    @PostMapping("/{reservationId}")
    @ApiOperation(value = "Reservation 상태 변경",
            notes = "요청 데이터(path) -> key(path) : reservationId, key(body) : " +
                    "status(PAYMENT_COMPLETED, REFUND_PROGRESSING, REFUND_COMPLETED")
    public APIResponse changeReservationStatus(
            @PathVariable("reservationId") Long reservationId,
            @RequestBody Map<String, PaymentStatus> request
    ) {
        reservationService.changeReservationStatus(reservationId, request.get("status"));

        return APIResponse.empty();
    }
    @GetMapping
    @ApiOperation(value = "(7) My plan - 일정 관리",
        notes = "요청 데이터(param) - key -> date")
    public APIResponse getMyReservation(@PK Long memberId,
                                        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return APIResponse.of(ReservationResponse.of(reservationService.getMyReservation(memberId, date)));
    }

}
