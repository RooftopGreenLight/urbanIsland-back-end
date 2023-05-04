package rooftopgreenlight.urbanisland.domain.reservation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentType {
    KAKAO_PAY("카카오페이");

    private String description;
}
