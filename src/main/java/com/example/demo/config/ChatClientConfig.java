package com.example.demo.config;

import com.example.demo.advisor.MyLoggingAdvisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class ChatClientConfig {

	@Bean
	ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
		return chatClientBuilder
			.defaultAdvisors(MyLoggingAdvisor.builder()
				.order(Ordered.HIGHEST_PRECEDENCE + 2000)
				.showConversationHistory(true)
				.build())
			.build();
	}

}
