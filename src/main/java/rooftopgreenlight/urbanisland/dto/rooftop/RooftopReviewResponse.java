package rooftopgreenlight.urbanisland.dto.rooftop;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopReview;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RooftopReviewResponse {

    private Long rooftopId;

    private int grade;
    private String content;
    private String memberNickname;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createTime;

    protected RooftopReviewResponse(Long rooftopId, int grade, String content) {
        this.rooftopId = rooftopId;
        this.grade = grade;
        this.content = content;
    }

    protected RooftopReviewResponse(int grade, String content, String memberNickname, LocalDateTime createTime) {
        this.grade = grade;
        this.content = content;
        this.memberNickname = memberNickname;
        this.createTime = createTime;
    }

    public static RooftopReviewResponse from(final RooftopReview rooftopReview) {
        return new RooftopReviewResponse(
                rooftopReview.getRooftop().getId(),
                rooftopReview.getGrade(),
                rooftopReview.getContent());
    }

    public static RooftopReviewResponse of(RooftopReviewDto rooftopReview) {
        return new RooftopReviewResponse(
                rooftopReview.getGrade(),
                rooftopReview.getContent(),
                rooftopReview.getMemberNickname(),
                rooftopReview.getCreateTime()
        );
    }
}
