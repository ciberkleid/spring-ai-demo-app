package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memory")
public class ChatMemoryController {

	private final ChatClient chatClient;

	private final ChatClient chatClientWithMemory;

	public ChatMemoryController(ChatClient chatClient) {
		this.chatClient = chatClient;

		var chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();
		this.chatClientWithMemory = chatClient.mutate()
			.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
			.build();
	}

	@GetMapping("/chat")
	public String chat(@RequestParam String message) {
		return chatClient.prompt(message)
			.call()
			.content();
	}

	@GetMapping("/chat-with-memory")
	public String chatWithMemory(@RequestParam String message, @RequestParam(defaultValue = "default") String conversationId) {
		return chatClientWithMemory.prompt(message)
			.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
			.call()
			.content();
	}

}
