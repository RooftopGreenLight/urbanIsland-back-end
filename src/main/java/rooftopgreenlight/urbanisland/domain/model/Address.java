package rooftopgreenlight.urbanisland.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String district;
    private String detail;

    protected Address(String city, String district, String detail) {
        this.city = city;
        this.district = district;
        this.detail = detail;
    }

    public static Address of(String city, String district, String detail) {
        return new Address(city, district, detail);
    }
}
