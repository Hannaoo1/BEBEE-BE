package com.lgcns.bebee.chat.core.config;

import com.lgcns.bebee.chat.core.utils.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenValidator jwtTokenValidator;

    /**
     * 메시지 전송 전에 호출됩니다.
     * CONNECT 명령에서 인증 정보를 추출하여 Principal을 설정합니다.
     *
     * @param message 전송될 메시지
     * @param channel 메시지 채널
     * @return 처리된 메시지
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            // Authorization 헤더에서 토큰 추출
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                try {
                    Claims claims = jwtTokenValidator.parseClaims(token);
                    Long memberId = jwtTokenValidator.extractMemberId(claims);

                    MemberPrincipal principal = new MemberPrincipal(memberId);
                    accessor.setUser(principal);
                    log.info("WebSocket authentication successful - Member ID: {}", memberId);
                } catch (JwtException e) {
                    log.warn("WebSocket authentication failed - Invalid token: {}", e.getMessage());
                }
            } else {
                log.warn("WebSocket connection without authentication token");
            }
        }

        return message;
    }
}