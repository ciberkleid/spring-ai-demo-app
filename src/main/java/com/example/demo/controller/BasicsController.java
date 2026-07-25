package com.example.demo.controller;

import java.nio.charset.Charset;
import java.util.List;

import com.example.demo.model.ActorsFilms;
import com.example.demo.model.Track;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/basics")
public class BasicsController {

	private final ChatClient chatClient;

	@Value("classpath:spring-io-2025-schedule.md")
	private Resource conferenceAgenda;

	public BasicsController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/system-instructions")
	public String systemInstructions(@RequestParam(defaultValue = "Tell me a joke?") String message) {
		return chatClient.prompt()
			.system("Impersonate Yoda (from Star Wars). Keep the jokes clean, short and family friendly.")
			.user(message)
			.call()
			.content();
	}

	@GetMapping("/structured-output")
	public ActorsFilms structuredOutput(@RequestParam(defaultValue = "Tom Hanks") String actor) {
		return chatClient.prompt()
			.user("Generate the filmography of 5 movies for %s.".formatted(actor))
			.call()
			.entity(ActorsFilms.class);
	}

	@GetMapping("/prompt-stuffing")
	public List<Track> promptStuffing() {
		return chatClient.prompt()
			.system("You are a useful assistant. Follow the user instructions.")
			.user(u -> u.text("""
					Get the list of talks grouped by tracks :
					{additionalContext}.
					List only the sessions with more than 1 speakers""").param("additionalContext", asText(conferenceAgenda)))
			.call()
			.entity(new ParameterizedTypeReference<List<Track>>() {
			});
	}

	private static String asText(Resource resource) {
		try {
			return resource.getContentAsString(Charset.defaultCharset());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
