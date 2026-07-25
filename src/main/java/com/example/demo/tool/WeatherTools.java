package com.example.demo.tool;

import java.time.LocalDateTime;

import org.slf4j.Logger;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherTools {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WeatherTools.class);

	private final RestClient restClient;

	public WeatherTools() {
		this.restClient = RestClient.create();
	}

	public record WeatherResponse(Current current) {
		public record Current(LocalDateTime time, int interval, double temperature_2m) {
		}
	}

	@Tool(description = "Get the temperature (in celsius) for a specific location")
	public WeatherResponse getTemperature(@ToolParam(description = "The location latitude") double latitude,
			@ToolParam(description = "The location longitude") double longitude,
			@ToolParam(description = "The city name") String city) {

		WeatherResponse response = restClient
				.get()
				.uri("https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m",
						latitude, longitude)
				.retrieve()
				.body(WeatherResponse.class);

		logger.debug("Check temparature for {}. Lat: {}, Lon: {}. Temp: {}", city, latitude, longitude,
				response.current);

		return response;
	}
}
