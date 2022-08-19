package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopReview;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopReviewResponse {

    private long rooftopId;

    private int grade;
    private String content;

    public static RooftopReviewResponse from(final RooftopReview rooftopReview) {
        return new RooftopReviewResponse(
                rooftopReview.getRooftop().getId(),
                rooftopReview.getGrade(),
                rooftopReview.getContent());
    }
}
