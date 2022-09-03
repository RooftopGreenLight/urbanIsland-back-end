package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReservationResponse {

    private Long reservationId;

    private String tid;

    protected ReservationResponse(Long reservationId, String tid) {
        this.reservationId = reservationId;
        this.tid = tid;
    }

    public ReservationResponse fromReservationId(Long reservationId) {
        return new ReservationResponse(reservationId, null);
    }

    public ReservationResponse fromTid(String tid) {
        return new ReservationResponse(null, tid);
    }
}
