package rooftopgreenlight.urbanisland.domain.rooftop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopType;

public interface RooftopRepository extends
        JpaRepository<Rooftop, Long>,
        QuerydslPredicateExecutor<Rooftop> {

    @Query("select distinct r from Rooftop r join r.rooftopImages ri join r.rooftopOptions ro " +
            "join r.rooftopDetails rd where r.rooftopType = :type")
    Page<Rooftop> findByNGRooftopPage(@Param("type") RooftopType type, Pageable pageable);
}
