package rooftopgreenlight.urbanisland.domain.rooftop.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopAddress {

    private String county;
    private String city;
    private String detail;

    protected RooftopAddress(String county, String city, String detail) {
        this.county = county;
        this.city = city;
        this.detail = detail;
    }

    public static RooftopAddress of(String county, String city, String detail) {
        return new RooftopAddress(county, county, detail);
    }
}
