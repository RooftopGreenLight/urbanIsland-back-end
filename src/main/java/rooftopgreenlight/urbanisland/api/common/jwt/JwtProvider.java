package rooftopgreenlight.urbanisland.api.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rooftopgreenlight.urbanisland.api.common.jwt.dto.TokenDto;
import rooftopgreenlight.urbanisland.api.common.properties.JwtProperties;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private Key key;
    private final JwtProperties jwtProperties;

    @PostConstruct
    private void createKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createJwt(String id, String authorities) {
        Date expirationDate = new Date(System.currentTimeMillis() + Long.parseLong(jwtProperties.getExpirationTime()));

        String token = Jwts.builder()
                .setSubject(id)
                .claim("authority", authorities)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();


        return TokenDto.createTokenDto()
                .type(jwtProperties.getType())
                .accessToken(token)
                .refreshToken(createRefreshToken(id, authorities))
                .build();
    }

    private String createRefreshToken(String id, String authorities) {
        Date refreshExpirationDate =
                new Date(System.currentTimeMillis() + Long.parseLong(jwtProperties.getRefreshExpirationTime()));

        return Jwts.builder()
                .setSubject(id)
                .claim("authority", authorities)
                .setExpiration(refreshExpirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getMemberInfo(String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        String memberId = claims.getSubject();
        String roleStr = claims.get("authority").toString();

        String[] roles = roleStr.split(" ");
        List<SimpleGrantedAuthority> roleList = Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        UserDetails principal = new User(memberId, "", roleList);

        return new UsernamePasswordAuthenticationToken(principal, "", roleList);
    }
}
