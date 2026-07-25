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

	public ChatMemoryController(ChatClient chatClient) {
		var chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();
		this.chatClient = chatClient.mutate()
			.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
			.build();
	}

	@GetMapping("/chat")
	public String chat(@RequestParam String message, @RequestParam(defaultValue = "default") String conversationId) {
		return chatClient.prompt(message)
			.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
			.call()
			.content();
	}

}
