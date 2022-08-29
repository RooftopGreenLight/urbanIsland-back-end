package rooftopgreenlight.urbanisland.domain.greenbee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.GreenBee;

import java.util.List;
import java.util.Optional;

public interface GreenBeeRepository extends
        JpaRepository<GreenBee, Long>,
        QuerydslPredicateExecutor<GreenBee>,
        GreenBeeRepositoryCustom {

    @Query("select g from GreenBee g left join fetch g.greenBeeImages gi where g.member.id = :memberId")
    Optional<GreenBee> findByIdWithImages(@Param("memberId") Long memberId);

    @Query("select g from GreenBee g left join fetch g.greenBeeImages gi where g.member.id = :memberId")
    Optional<GreenBee> findByMemberIdWithImages(@Param("memberId") Long memberId);

    @Query("select g from GreenBee g left join fetch g.member gm where gm.id = :memberId")
    Optional<GreenBee> findByMemberIdWithMember(@Param("memberId") Long memberId);

    boolean existsByMemberId(Long memberId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from GreenBeeImage gi where gi.storeFilename in :imageNames")
    void deleteImagesByFileName(@Param("imageNames") List<String> imageNames);

}
