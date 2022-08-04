package rooftopgreenlight.urbanisland.domain.rooftop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;

public interface RooftopRepository extends
        JpaRepository<Rooftop, Long>,
        QuerydslPredicateExecutor<Rooftop> {
}
