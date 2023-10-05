package com.cryptic.bingo.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.cryptic.bingo.security.SecurityConstants;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setAllowedOrigins(SecurityConstants.ALLOWED_ORIGINS);
		registry.addEndpoint("/ws").setAllowedOrigins(SecurityConstants.ALLOWED_ORIGINS).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/game");
		registry.setUserDestinationPrefix("/game");
		registry.setApplicationDestinationPrefixes("/app");
	}

}
