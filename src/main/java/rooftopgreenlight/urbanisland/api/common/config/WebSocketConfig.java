package rooftopgreenlight.urbanisland.api.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import rooftopgreenlight.urbanisland.api.common.socket.handler.StompHandler;

/**
 * 나중에 커밋할 때 지울 내용
 * - subscribe를 통해 통신하려는 주체(topic)을 판단
 * - Broker(브로커)를 통해 실시간, 지속적 관심을 가지며 해당 요청이 들어오면 처리
 *
 * Connect : 버전 정보, heart-beat, user-name (세션 정보 - security 이용)
 * Subscribe : id, destination (현재 메시지에 대한 목적지 설정하기)
 * 옥상지기 <-> 소비자 사이의 채팅방을 만드는 거니까...
 * 목적지를 그럼 대충 reservationId를 기준으로 해서 나눠야 할 것 같다.
 * Message : 메시지 전송 시 구조
 * destination(목적지), content-type (json으로 많이 함), subscription,
 * messageId, content-Id, 그리고 message에 대한 JSON 내용 (userId-message 정보)
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler; // jwt 인증.

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 엔드포인트에 interceptor를 추가해서 소켓 등록.
        registry.addEndpoint("/ws/api/v1/chat/websocket")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 브로커 등록 코드.
        // /queue로 시작하는 주소를 구독한 subscriber에게 메시지 전송
        registry.enableSimpleBroker("/queue");
        // prefix 설정. /app으로 하면 Broker에게 보내진다.
        registry.setApplicationDestinationPrefixes("/app");
    }

    // 인터셉터를 통해 stompHandler 지정
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

}
