package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
public class RagController {

	private final ChatClient chatClient;

	public RagController(ChatClient chatClient, VectorStore vectorStore) {
		this.chatClient = chatClient.mutate()
			.defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
			.build();
	}

	@GetMapping("/hurricane")
	public String hurricane(@RequestParam(defaultValue = "Was Florida hit by the Hurricane Milton?") String question) {
		return chatClient.prompt().user(question).call().content();
	}

}
