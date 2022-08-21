package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopReviewDto {
    private int grade;
    private String content;
    private LocalDateTime createTime;
    private String memberNickname;


    public static RooftopReviewDto of(int grade, String content, LocalDateTime createTime, String memberNickname) {
        return new RooftopReviewDto(grade, content, createTime, memberNickname);
    }
}
