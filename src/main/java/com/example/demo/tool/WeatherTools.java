package com.example.demo.tool;

import java.time.LocalDateTime;
import java.util.List;

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

	private record GeocodingResponse(List<Result> results) {
		private record Result(double latitude, double longitude) {
		}
	}

	@Tool(description = "Get the temperature (in celsius) for a specific city")
	public WeatherResponse getTemperature(@ToolParam(description = "The city name") String city) {

		GeocodingResponse geocoding = restClient
				.get()
				.uri("https://geocoding-api.open-meteo.com/v1/search?name={city}&count=1", city)
				.retrieve()
				.body(GeocodingResponse.class);

		if (geocoding == null || geocoding.results() == null || geocoding.results().isEmpty()) {
			throw new IllegalArgumentException("Could not find coordinates for city: " + city);
		}

		var location = geocoding.results().get(0);

		WeatherResponse response = restClient
				.get()
				.uri("https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m",
						location.latitude(), location.longitude())
				.retrieve()
				.body(WeatherResponse.class);

		logger.debug("Check temparature for {}. Lat: {}, Lon: {}. Temp: {}", city, location.latitude(),
				location.longitude(), response.current);

		return response;
	}
}
