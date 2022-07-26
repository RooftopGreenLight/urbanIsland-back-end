package rooftopgreenlight.urbanisland.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import rooftopgreenlight.urbanisland.api.common.exception.MailSendException;
import rooftopgreenlight.urbanisland.api.common.jwt.JwtProvider;
import rooftopgreenlight.urbanisland.api.common.jwt.dto.TokenDto;
import rooftopgreenlight.urbanisland.api.common.properties.MailProperties;

import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    private final AuthenticationManager authenticationManager;

    public TokenDto login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        String authorities = authenticate.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.joining(" "));

        return jwtProvider.createJwt(authenticate.getName(), authorities);
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

}
