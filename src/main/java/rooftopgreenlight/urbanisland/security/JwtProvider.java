package rooftopgreenlight.urbanisland.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import rooftopgreenlight.urbanisland.dto.member.TokenDto;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static rooftopgreenlight.urbanisland.constant.RedisKey.REFRESH_TOKEN_KEY;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private Key key;
    private final JwtProperties jwtProperties;
    private final RedisTemplate redisTemplate;

    @PostConstruct
    private void createKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createJwt(String id, String email, String authorities, String refreshToken) {
        Date expirationDate = new Date(System.currentTimeMillis() + Long.parseLong(jwtProperties.getExpirationTime()));

        String token = Jwts.builder()
                .setSubject(id)
                .claim("authority", authorities)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        if (refreshToken == null) {
            refreshToken = createRefreshToken(id, authorities);
            redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY + email, refreshToken,
                    Long.parseLong(jwtProperties.getRefreshExpirationTime()), TimeUnit.MILLISECONDS);
        }

        return TokenDto.of(jwtProperties.getType(), token, refreshToken);
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

    public boolean isTokenValid(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
            return !jwtParser.parseClaimsJws(token).getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            if(e instanceof ExpiredJwtException) {
                return false;
            }
        }
        return false;
    }

}
