package rooftopgreenlight.urbanisland.domain.reservation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentStatus {

    PAYMENT_PROGRESSING("결제 진행 중"), PAYMENT_COMPLETED("결제 완료"), REFUND_PROGRESSING("환불 처리 중"), REFUND_COMPLETED("환불 완료");

    private String description;

}
