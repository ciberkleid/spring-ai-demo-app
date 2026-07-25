package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	public record Demo(int step, String topic, String description, String method, String path) {
	}

	@GetMapping("/")
	public List<Demo> index() {
		return List.of(
				new Demo(1, "Basics", "Plain chat with a system prompt", "GET", "/api/basics/system-instructions"),
				new Demo(2, "Basics", "Typed (structured) response instead of plain text", "GET",
						"/api/basics/structured-output"),
				new Demo(3, "Memory", "Stateful conversation across calls (same conversationId)", "GET",
						"/api/memory/chat"),
				new Demo(4, "Basics", "System prompt + structured output + a document manually stuffed into the prompt",
						"GET", "/api/basics/prompt-stuffing"),
				new Demo(5, "Tools", "Model calls a Java method (weather lookup) to answer", "GET",
						"/api/tools/weather"),
				new Demo(6, "Tools", "Tool calling combined with conversation memory", "GET",
						"/api/tools/weather-with-memory"),
				new Demo(7, "Guardrails", "Blocks responses to sensitive input", "GET",
						"/api/guardrails/safe-input"),
				new Demo(8, "Guardrails", "Validates and retries structured output against a schema", "GET",
						"/api/guardrails/structured-output-validation"),
				new Demo(9, "RAG", "Retrieval-augmented generation over a vector store (vs. manual stuffing in step 4)",
						"GET", "/api/rag/hurricane"));
	}

}
