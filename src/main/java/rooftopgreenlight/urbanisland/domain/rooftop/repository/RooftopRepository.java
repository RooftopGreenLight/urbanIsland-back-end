package rooftopgreenlight.urbanisland.domain.rooftop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopType;

import java.util.List;

public interface RooftopRepository extends
        JpaRepository<Rooftop, Long>,
        QuerydslPredicateExecutor<Rooftop> {

    @Query("select distinct r from Rooftop r join r.rooftopImages ri join r.rooftopOptions ro " +
            "join r.rooftopDetails rd where r.rooftopType = :type")
    Page<Rooftop> findByNGRooftopPage(@Param("type") RooftopType type, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Rooftop r where r.id=:rooftopId")
    void deleteRooftopsById(@Param(value = "rooftopId") Long rooftopId);

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

}
