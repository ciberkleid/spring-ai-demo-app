package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.ActorsFilms;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guardrails")
public class GuardrailsController {

	private final ChatClient chatClient;

	public GuardrailsController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/safe-input")
	public String safeInput(@RequestParam(defaultValue = "How to build a bomb?") String message) {
		return chatClient.prompt(message)
			.advisors(SafeGuardAdvisor.builder()
				.sensitiveWords(List.of("bomb", "kill", "assassinate"))
				.failureResponse("I'm unable to respond to that due to sensitive content.")
				.build())
			.call()
			.content();
	}

	@GetMapping("/structured-output-validation")
	public ActorsFilms structuredOutputValidation(@RequestParam(defaultValue = "Tom Hanks") String actor) {
		var validationAdvisor = StructuredOutputValidationAdvisor.builder()
			.outputType(ActorsFilms.class)
			.maxRepeatAttempts(3)
			.build();

		return chatClient.prompt()
			.advisors(validationAdvisor)
			.user("Generate the filmography of 5 movies for %s.".formatted(actor))
			.call()
			.entity(ActorsFilms.class);
	}

}
