package rooftopgreenlight.urbanisland.domain.rooftop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopReview;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopType;

import java.util.List;
import java.util.Optional;

public interface RooftopRepository extends
        JpaRepository<Rooftop, Long>,
        QuerydslPredicateExecutor<Rooftop>,
        RooftopRepositoryCustom
{

    @Query("select distinct r from Rooftop r join r.rooftopImages ri join r.rooftopOptions ro " +
            "join r.rooftopDetails rd where r.rooftopType = :type")
    Page<Rooftop> findByNGRooftopPage(@Param("type") RooftopType type, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Rooftop r where r.id=:rooftopId")
    void deleteRooftopsById(@Param(value = "rooftopId") Long rooftopId);

    @Query("select r from Rooftop r where r.rooftopProgress = :progress")
    Page<Rooftop> findRooftopPageByProgress(@Param("progress")Progress progress, Pageable pageable);

    @Query("select r from Rooftop r left join fetch r.reviews rr where r.id = :rooftopId")
    Optional<Rooftop> findByIdWithReview(@Param("rooftopId") final Long rooftopId);

    @Query("select r from Rooftop r join fetch r.member rm where r.id=:rooftopId")
    Optional<Rooftop> findRooftopWithMember(@Param("rooftopId") Long rooftopId);

    @Query("select r from Rooftop r left join r.reviews rr left join r.rooftopImages ri left join r.rooftopOptions ro " +
            "left join r.rooftopDetails rd where r.id = :rooftopId")
    Optional<Rooftop> findRooftopWithAll(@Param("rooftopId") Long rooftopId);

    @Query("select r from Rooftop r where r.member.id=:memberId and r.rooftopType='GREEN'" +
            "and r.rooftopProgress in (:progress)")
    List<Rooftop> findGreenRooftopByMemberId(@Param("memberId") Long memberId, @Param("progress") List<Progress> progress);

    @Query("select r from Rooftop r where r.member.id =:memberId and r.rooftopProgress in (:progress)")
    List<Rooftop> findNGRooftopByMemberId(@Param("memberId") Long memberId, @Param("progress") List<Progress> progress);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RooftopDetail r where r.rooftop.id=:rooftopId")
    void deleteRooftopDetails(@Param(value = "rooftopId") Long rooftopId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RooftopOption r where r.rooftop.id=:rooftopId")
    void deleteRooftopOptions(@Param(value = "rooftopId") Long rooftopId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RooftopImage r where r.rooftop.id=:rooftopId")
    void deleteRooftopImages(@Param(value = "rooftopId") Long rooftopId);

    @Query("select r from RooftopImage r where r.rooftop.id=:rooftopId")
    List<RooftopImage> findRooftopImagesByRooftopId(@Param(value = "rooftopId") Long rooftopId);

    @Query("select rr from RooftopReview rr where rr.id = :reviewId")
    Optional<RooftopReview> findRooftopReviewByRooftopReviewId(final @Param("reviewId") long reviewId);

    @Modifying
    @Query("delete from RooftopReview rr where rr.id = :reviewId")
    void deleteRooftopReviewByRooftopReviewId(final @Param("reviewId") long reviewId);

    @Query("select rr from RooftopReview rr where rr.member.id = :memberId")
    Page<RooftopReview> findRooftopReviewPageByMemberId(final @Param("memberId") long memberId, Pageable pageable);

    @Query("select r from Rooftop r where r.member.id = :memberId and r.rooftopType = 'GREEN'")
    Page<Rooftop> findByMyRooftopInfo(@Param("memberId") Long memberId, Pageable pageable);
}
