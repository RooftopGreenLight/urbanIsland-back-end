package rooftopgreenlight.urbanisland.domain.rooftop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopGreeningApply;

import java.util.List;
import java.util.Optional;

public interface RooftopGreeningApplyRepository extends JpaRepository<RooftopGreeningApply, Long> {

    @Query("select distinct r from RooftopGreeningApply r join r.rooftop rt join r.greenBee rg " +
            "where rt.member.id = :memberId and not r.greeningProgress='ADMIN_COMPLETED'")
    Page<RooftopGreeningApply> getGreenBeeWaitingList(@Param(value = "memberId") Long memberId,
                                                      Pageable pageable);


    @Query("select r from RooftopGreeningApply r join r.rooftop rt where rt.id =:rooftopId")
    List<RooftopGreeningApply> getRooftopApply(@Param(value = "rooftopId") Long rooftopId);

    @Query("select distinct r from RooftopGreeningApply r join r.rooftop rt where r.greenBeeMemberId =:memberId " +
            "and r.greeningProgress=:progress")
    List<RooftopGreeningApply> getGreeningRooftopOfGreenBee(@Param(value = "memberId") Long memberId,
                                                            @Param(value = "progress") Progress progress);

    @Query("select distinct r from RooftopGreeningApply r join r.rooftop rt join r.greenBee gb where r.greenBeeMemberId=:memberId " +
            "and r.greeningProgress in (:progress)")
    List<RooftopGreeningApply> getSelectedRooftopOfGreenBee(@Param(value = "memberId") Long memberId,
                                                            @Param(value = "progress") List<Progress> progress);

    @Query("select r from RooftopGreeningApply r where r.rooftop.id =:rooftopId and r.greenBeeMemberId=:memberId")
    Optional<RooftopGreeningApply> getRooftopApplyByGreenBeeId(Long rooftopId, Long memberId);
}
