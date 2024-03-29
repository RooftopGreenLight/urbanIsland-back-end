package rooftopgreenlight.urbanisland.domain.rooftop;

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
public class RooftopPeopleCount {

    private int adultCount;
    private int kidCount;
    private int petCount;
    private int totalCount;

    protected RooftopPeopleCount(int adultCount, int kidCount, int petCount, int totalCount) {
        this.adultCount = adultCount;
        this.kidCount = kidCount;
        this.petCount = petCount;
        this.totalCount = totalCount;
    }

    public static RooftopPeopleCount of(int adultCount, int kidCount, int petCount, int totalCount) {
        return new RooftopPeopleCount(adultCount, kidCount, petCount, totalCount);
    }

    public RooftopPeopleCount updatePeopleCount(Integer adultCount, Integer kidCount, Integer petCount, Integer totalCount) {
        if(adultCount != null) this.adultCount = adultCount;
        if(kidCount != null) this.kidCount = kidCount;
        if(petCount != null) this.petCount = petCount;
        if(totalCount != null) this.totalCount = totalCount;
        return this;
    }
}
