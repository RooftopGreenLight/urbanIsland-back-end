package rooftopgreenlight.urbanisland.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import rooftopgreenlight.urbanisland.domain.member.Member;

@Repository
public interface MemberRepository extends
        JpaRepository<Member, Long>,
        QuerydslPredicateExecutor<Member> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findMemberByEmail(String email);
    @Query("select m from Member m left join fetch m.profile p where m.id = :memberId")
    Optional<Member> findMemberByMemberIdWithProfile(@Param("memberId") Long memberId);
}
