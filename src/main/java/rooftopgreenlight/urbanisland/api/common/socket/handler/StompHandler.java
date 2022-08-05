package rooftopgreenlight.urbanisland.api.common.socket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import rooftopgreenlight.urbanisland.api.common.jwt.JwtProvider;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtProvider jwtProvider;

    // 클라이언트가 connect 시 헤더로 보낸 Authorization에 담긴 JWT 검증.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String authorization = accessor.getFirstNativeHeader("Authorization");
        String token = authorization.replace("Bearer ", "");

        log.info("authorization = {}", authorization);
        log.info("token = {}", token);
        log.info("accessor = {}", accessor);
        log.info("accessor.getCommand = {}", accessor.getCommand());
        log.info("message = {}", message);
        log.info("channel = {}", channel);

        if(accessor.getCommand() == StompCommand.CONNECT) {
            if(!jwtProvider.isTokenValid(token)) {
                // 우선 RuntimeException으로 반환함!
                throw new RuntimeException("Not Valid Token");
            }
        }
        return message;
    }
}
