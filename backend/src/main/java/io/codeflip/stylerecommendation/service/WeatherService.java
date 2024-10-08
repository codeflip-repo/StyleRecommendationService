package io.codeflip.stylerecommendation.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
public class WeatherService {
@Value("${openweathermap.api.key}")
private String apiKey;

private final WebClient webClient;

public WeatherService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://api.openweathermap.org/data/2.5").build();
}

public Mono<String> getWeather(String location) {
    return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/weather")
                .queryParam("q", location)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .build())
            .retrieve()
            .bodyToMono(String.class);
}
}
