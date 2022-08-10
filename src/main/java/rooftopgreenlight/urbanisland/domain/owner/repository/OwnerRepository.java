package rooftopgreenlight.urbanisland.domain.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.owner.entity.Owner;

import java.util.Optional;

public interface OwnerRepository extends
        JpaRepository<Owner, Long>,
        QuerydslPredicateExecutor<Owner>,
        OwnerRepositoryCustom{

    @Query("select o from Owner o left join fetch o.member om where o.member.id = :memberId")
    Optional<Owner> findByMemberIdWithMember(@Param("memberId") Long memberId);

    @Query("select o from Owner o left join fetch o.ownerImage oi where o.member.id = :memberId")
    Optional<Owner> findByMemberIdWithImage(@Param("memberId") Long memberId);
}
