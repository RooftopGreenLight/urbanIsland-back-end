package rooftopgreenlight.urbanisland.dto.rooftop;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopReview;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopReviewPageResponse {

    private int totalPages;
    private long totalElements;
    private List<RooftopReviewResponse> rooftopResponses;

    public static RooftopReviewPageResponse from(final Page<RooftopReview> pageRooftopReviews) {
        return new RooftopReviewPageResponse(
                pageRooftopReviews.getTotalPages(),
                pageRooftopReviews.getTotalElements(),
                pageRooftopReviews.getContent().stream()
                        .map(RooftopReviewResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
