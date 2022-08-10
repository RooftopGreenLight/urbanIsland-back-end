package rooftopgreenlight.urbanisland.domain.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import rooftopgreenlight.urbanisland.domain.owner.entity.Owner;

public interface OwnerRepository extends
        JpaRepository<Owner, Long>,
        QuerydslPredicateExecutor<Owner>,
        OwnerRepositoryCustom{
}
