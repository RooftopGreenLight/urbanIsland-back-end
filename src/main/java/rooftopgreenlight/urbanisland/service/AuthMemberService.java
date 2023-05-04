package rooftopgreenlight.urbanisland.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rooftopgreenlight.urbanisland.domain.member.Member;
import rooftopgreenlight.urbanisland.service.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthMemberService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberService.findByEmail(username);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(findMember.getAuthority().toString());
        return new User(String.valueOf(findMember.getId()), findMember.getPassword(), List.of(authority));
    }
}
