package rooftopgreenlight.urbanisland.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByRefreshToken(String refreshToken);
}
