package rooftopgreenlight.urbanisland.domain.rooftop.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopAddress {

    @Column(name = "address_city", nullable = false)
    private String city;
    @Column(name = "address_district", nullable = false)
    private String district;
    @Column(name = "address_detail", nullable = false)
    private String detail;

    protected RooftopAddress(String city, String district, String detail) {
        this.city = city;
        this.district = district;
        this.detail = detail;
    }

    public static RooftopAddress of(String city, String district, String detail) {
        return new RooftopAddress(city, district, detail);
    }
}
