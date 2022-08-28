package rooftopgreenlight.urbanisland.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import rooftopgreenlight.urbanisland.api.common.exception.ExpiredRefreshTokenException;
import rooftopgreenlight.urbanisland.api.common.exception.AccessException;
import rooftopgreenlight.urbanisland.api.common.exception.MailSendException;
import rooftopgreenlight.urbanisland.api.common.exception.NotMatchedRefreshTokenException;
import rooftopgreenlight.urbanisland.api.common.jwt.JwtProvider;
import rooftopgreenlight.urbanisland.api.common.jwt.dto.TokenDto;
import rooftopgreenlight.urbanisland.api.common.properties.JwtProperties;
import rooftopgreenlight.urbanisland.api.controller.dto.MemberResponse;
import rooftopgreenlight.urbanisland.api.common.properties.MailProperties;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;

import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static rooftopgreenlight.urbanisland.api.util.constant.RedisKey.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;
    private final MemberService memberService;
    private final JwtProperties jwtProperties;
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public MemberResponse login(String email, String password) {
        isLoginValid(email);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        String authorities = authenticate.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.joining(" "));

        String id = authenticate.getName();
        TokenDto tokenDto = jwtProvider.createJwt(id, email, authorities, null);

        return MemberResponse.of(Long.valueOf(id), authorities, tokenDto);
    }

    public TokenDto checkRefreshToken(String refreshToken, Long memberId) {
        Member findMember = memberService.findById(memberId);
        String findRefreshToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + findMember.getEmail());

        if(!jwtProvider.isTokenValid(refreshToken)) {
            throw new ExpiredRefreshTokenException("Refresh-token is not valid. Please Re-Login.");
        }
        if(!findRefreshToken.equals(refreshToken)) {
            throw new NotMatchedRefreshTokenException("Refresh-token is not matched. Please Re-Login.");
        }

        return jwtProvider.createJwt(String.valueOf(memberId), findMember.getEmail(),
                findMember.getAuthority().toString(), refreshToken);
    }

    public String send(String email) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<8; i++) {
            sb.append(random.nextInt(10));
            if(i==3)
                sb.append("-");
        }

        String key = sb.toString();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;

        try {
            mimeMessageHelper = new MimeMessageHelper(message, false, "UTF-8");
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("UrbanIsland 인증 메일입니다.");
            mimeMessageHelper.setText("인증 번호: " + key);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new MailSendException("메일 전송에 실패하였습니다. " + e.getMessage());
        }
        return key;
    }

    public void logout(Long memberId, String token) {
        Member findMember = memberService.findById(memberId);

        redisTemplate.delete(REFRESH_TOKEN_KEY + findMember.getEmail());
        String substring = token.substring(7);
        System.out.println("substring = " + substring);
        redisTemplate.opsForValue().set(token.substring(7), String.valueOf(memberId),
                Long.parseLong(jwtProperties.getExpirationTime()), TimeUnit.MILLISECONDS);
    }

    private void isLoginValid(String email) {
        String refreshToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + email);

        if (StringUtils.hasText(refreshToken)) {
            throw new AccessException("이미 로그인 중인 회원입니다.");
        }
    }
}
