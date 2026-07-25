package com.example.demo.controller;

import com.example.demo.tool.WeatherTools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tools")
public class ToolCallingController {

	private static final String DEFAULT_QUESTION = "What should I wear today in Amsterdam and in Barcelona?";

	private final ChatClient chatClient;

	private final ChatClient chatClientWithMemory;

	private final WeatherTools weatherTools;

	public ToolCallingController(ChatClient chatClient, WeatherTools weatherTools) {
		this.chatClient = chatClient;
		this.weatherTools = weatherTools;

		var chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();
		this.chatClientWithMemory = chatClient.mutate()
			.defaultAdvisors(
					ToolCallAdvisor.builder().disableInternalConversationHistory().build(),
					MessageChatMemoryAdvisor.builder(chatMemory).order(Ordered.HIGHEST_PRECEDENCE + 1000).build())
			.build();
	}

	@GetMapping("/weather")
	public String weather(@RequestParam(defaultValue = DEFAULT_QUESTION) String question) {
		return chatClient.prompt()
			.tools(weatherTools)
			.advisors(ToolCallAdvisor.builder().build())
			.user(question)
			.call()
			.content();
	}

	@GetMapping("/weather-with-memory")
	public String weatherWithMemory(@RequestParam(defaultValue = DEFAULT_QUESTION) String question,
			@RequestParam(defaultValue = "default") String conversationId) {
		return chatClientWithMemory.prompt()
			.tools(weatherTools)
			.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
			.user(question)
			.call()
			.content();
	}

}
