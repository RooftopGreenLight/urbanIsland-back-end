package rooftopgreenlight.urbanisland.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import rooftopgreenlight.urbanisland.api.common.jwt.JwtProvider;
import rooftopgreenlight.urbanisland.api.common.jwt.dto.TokenDto;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public TokenDto login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        String authorities = authenticate.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.joining(" "));

        return jwtProvider.createJwt(authenticate.getName(), authorities);
    }

}
