package rooftopgreenlight.urbanisland.api.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            if (!isAuthentication(response, token)) return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthentication(HttpServletResponse response, String token) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        if(!jwtProvider.isTokenValid(token)) {
            createErrorResponse(response, objectMapper, ErrorCode.JWT_ACCESS_ERROR);
            return false;
        }
        Authentication authentication = jwtProvider.getMemberInfo(token);

        if (!isAccessValid(token)) {
            createErrorResponse(response, objectMapper, ErrorCode.ACCESS_ERROR);
            return false;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    private boolean isAccessValid(String token) {
        if (redisTemplate.hasKey(token)) {
            return false;
        }

        return true;
    }

    private static void createErrorResponse(HttpServletResponse response,
                                            ObjectMapper objectMapper,
                                            ErrorCode errorCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");

        response.getWriter().write(objectMapper.writeValueAsString(APIErrorResponse.of(false, errorCode)));
    }
}
