package rooftopgreenlight.urbanisland.api.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rooftopgreenlight.urbanisland.api.common.exception.error.ErrorCode;
import rooftopgreenlight.urbanisland.api.controller.dto.APIErrorResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(!request.getRequestURI().startsWith("/api/v1/auth") &&
                !request.getRequestURI().startsWith("/api/v1/oauth2") &&
                authorization != null &&
                authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            if(!jwtProvider.isTokenValid(token)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");

                response.getWriter().write(objectMapper.writeValueAsString(APIErrorResponse.of(false, ErrorCode.JWT_ACCESS_ERROR)));
                return;
            }
            Authentication authentication = jwtProvider.getMemberInfo(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
