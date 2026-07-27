package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
public class RagController {

	private final ChatClient chatClient;

	private final ObjectProvider<VectorStore> vectorStoreProvider;

	private ChatClient ragChatClient;

	public RagController(ChatClient chatClient, ObjectProvider<VectorStore> vectorStoreProvider) {
		this.chatClient = chatClient;
		this.vectorStoreProvider = vectorStoreProvider;
	}

	@GetMapping("/hurricane")
	public String hurricane(@RequestParam(defaultValue = "Was Florida hit by the Hurricane Milton?") String question) {
		return ragChatClient().prompt().user(question).call().content();
	}

	// For demo purposes, build the RAG-augmented ChatClient on demand rather than in the constructor, so
	// that the PDF ingestion and vectorization happens on first request, not at startup.
	private synchronized ChatClient ragChatClient() {
		if (this.ragChatClient == null) {
			this.ragChatClient = chatClient.mutate()
				.defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStoreProvider.getObject()).build())
				.build();
		}
		return this.ragChatClient;
	}

}
