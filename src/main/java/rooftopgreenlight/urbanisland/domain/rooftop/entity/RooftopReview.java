package rooftopgreenlight.urbanisland.domain.rooftop.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.common.BaseEntity;
import rooftopgreenlight.urbanisland.domain.common.exception.ExistObjectException;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Table(name = "rooftop_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RooftopReview extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private int grade;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rooftop_id")
    private Rooftop rooftop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void addRooftop(final Rooftop rooftop) {
        this.rooftop = rooftop;
        List<RooftopReview> reviews = rooftop.getReviews();

        reviews.forEach(review -> {
            if (review.getMember().getId().equals(this.member.getId())) {
                throw new ExistObjectException("해당 회원의 리뷰가 이미 존재합니다.");
            }
        });

        reviews.add(this);
    }

    @Builder(builderMethodName = "createReview")
    public RooftopReview(int grade, String content, Member member) {
        this.grade = grade;
        this.content = content;
        this.member = member;
    }
}
