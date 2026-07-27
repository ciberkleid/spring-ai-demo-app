package com.example.demo.controller;

import java.nio.charset.Charset;
import java.util.List;

import com.example.demo.model.ActorsFilms;
import com.example.demo.model.Track;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
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

	@Value("classpath:spring-io-2026-schedule.md")
	private Resource conferenceAgenda;

    // Trimmed content option for local ollama models for faster demo responses.
	@Value("classpath:spring-io-2026-schedule-trimmed.md")
	private Resource conferenceAgendaTrimmed;

	@Value("classpath:fifa-world-cup-2026-results.md")
	private Resource worldCupResults;

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
		var outputConverter = new BeanOutputConverter<>(ActorsFilms.class);

		String rawResponse = chatClient.prompt()
			.user(u -> u.text("Generate the filmography of 5 movies for {actor}.\n{format}")
				.param("actor", actor)
				.param("format", outputConverter.getFormat()))
			.call()
			.content();

		return outputConverter.convert(rawResponse);
	}

	@GetMapping("/prompt-stuffing")
	public List<Track> promptStuffing(@RequestParam(defaultValue = "true") boolean trimmed) {
        // Use trimmed content with local ollama models for faster demo responses.
		Resource agenda = trimmed ? conferenceAgendaTrimmed : conferenceAgenda;
		return chatClient.prompt()
			.system("You are a useful assistant. Follow the user instructions.")
			.user(u -> u.text("""
					Get the list of talks grouped by tracks :
					{additionalContext}.
					List only the sessions with more than 1 speakers""").param("additionalContext", asText(agenda)))
			.call()
			.entity(new ParameterizedTypeReference<List<Track>>() {
			});
	}

	@GetMapping("/prompt-stuffing-world-cup")
	public String promptStuffingWorldCup() {
		return chatClient.prompt()
			.system("You are an enthusiastic soccer commentator sharing tournament trivia with fellow fans.")
			.user(u -> u.text("""
					Here are the complete results of the FIFA World Cup 2026 :
					{results}.
					In 100 words, or less, share the tournament highlights and a few fun facts and stats that a soccer fan would enjoy.""")
				.param("results", asText(worldCupResults)))
			.call()
			.content();
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
