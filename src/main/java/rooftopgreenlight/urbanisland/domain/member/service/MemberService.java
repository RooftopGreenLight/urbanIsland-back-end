package rooftopgreenlight.urbanisland.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.domain.common.exception.NotFoundMemberException;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원 저장
     * @param member 저장할 회원
     * @return 저장된 회원
     */
    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    /**
     * id로 Member 찾기
     * @param id 회원 Id
     * @return 조회된 회원
     */
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> {throw new NotFoundMemberException("회원을 찾을 수 없습니다.");});
    }

    public Member findByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> {throw new NotFoundMemberException("회원을 찾을 수 없습니다.");});
    }

    /**
     * 존재하는 이메일인지 확인
     * @param email 검색할 이메일
     * @return True/False
     */
    public boolean existByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findMemberByRefreshToken(refreshToken)
                .orElseThrow(() -> {throw new NotFoundMemberException("회원을 찾을 수 없습니다.");});
    }
    @Transactional
    public Member changeRefreshToken(String refreshToken, Member findMember) {
        findMember.changeRefreshToken(refreshToken);
        return findMember;
    }

    public Member findByIdWithProfile(Long memberId) {
        return memberRepository.findMemberByMemberIdWithProfile(memberId).orElseThrow(() -> {
            throw new NotFoundMemberException("회원을 찾을 수 없습니다.");
        });
    }

    @Transactional
    public void changePassword(Long memberId, String password) {
        Member findMember = findById(memberId);
        findMember.changePassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void changePhoneNumber(Long memberId, String phoneNumber) {
        Member findMember = findById(memberId);
        findMember.changePhoneNumber(phoneNumber);
    }
}
