package rooftopgreenlight.urbanisland.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends
        JpaRepository<Member, Long>,
        QuerydslPredicateExecutor<Member> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByRefreshToken(String refreshToken);

    @Query("select m from Member m left join fetch m.profile p where m.id = :memberId")
    Optional<Member> findMemberByMemberIdWithProfile(@Param("memberId") Long memberId);
}
