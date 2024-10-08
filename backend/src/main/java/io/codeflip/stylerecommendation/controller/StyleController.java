package io.codeflip.stylerecommendation.controller;
import io.codeflip.stylerecommendation.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/style")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.4.196:3000"})
public class StyleController {
@Autowired
private WeatherService weatherService;

@GetMapping("/recommendation")
public Mono<Map<String, String>> getRecommendation(@RequestParam String location) {
    return weatherService.getWeather(location)
            .map(weatherData -> {
                JSONObject json = new JSONObject(weatherData);
                double temp = json.getJSONObject("main").getDouble("temp");
                String weatherDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");
                double feelsLike = json.getJSONObject("main").getDouble("feels_like");
                int humidity = json.getJSONObject("main").getInt("humidity");

                Map<String, String> recommendation = new HashMap<>();
                recommendation.put("weather", String.format("Current weather in %s: %.1f°C, feels like %.1f°C, %s, humidity: %d%%", 
                                                            location, temp, feelsLike, weatherDescription, humidity));
                
                String menRec = getRecommendationForMen(temp, weatherDescription);
                String womenRec = getRecommendationForWomen(temp, weatherDescription);
                String reasoning = getReasoningForRecommendation(temp, weatherDescription, humidity);
                
                recommendation.put("men", "For men in " + location + ": " + menRec);
                recommendation.put("women", "For women in " + location + ": " + womenRec);
                recommendation.put("reasoning", reasoning);
                
                return recommendation;
            })
            .onErrorResume(e -> {
                Map<String, String> defaultRecommendation = new HashMap<>();
                defaultRecommendation.put("weather", "Unable to fetch weather data for " + location);
                defaultRecommendation.put("men", "For men in " + location + ": Check the local weather and dress accordingly");
                defaultRecommendation.put("women", "For women in " + location + ": Check the local weather and dress accordingly");
                defaultRecommendation.put("reasoning", "No weather data available to provide specific recommendations.");
                return Mono.just(defaultRecommendation);
            });
}

private String getRecommendationForMen(double temp, String weatherDescription) {
    StringBuilder rec = new StringBuilder();
    if (temp < 10) {
        rec.append("A warm coat, thermal underclothes, ");
    } else if (temp < 20) {
        rec.append("A light jacket or sweater, ");
    } else {
        rec.append("A t-shirt or light shirt, ");
    }

    if (weatherDescription.contains("rain")) {
        rec.append("waterproof shoes, and an umbrella");
    } else if (weatherDescription.contains("snow")) {
        rec.append("warm boots, and a scarf");
    } else {
        rec.append("and comfortable shoes");
    }

    return rec.toString();
}

private String getRecommendationForWomen(double temp, String weatherDescription) {
    StringBuilder rec = new StringBuilder();
    if (temp < 10) {
        rec.append("A warm coat, thermal underclothes, ");
    } else if (temp < 20) {
        rec.append("A cardigan or light jacket, ");
    } else {
        rec.append("A light dress or blouse, ");
    }

    if (weatherDescription.contains("rain")) {
        rec.append("waterproof boots, and an umbrella");
    } else if (weatherDescription.contains("snow")) {
        rec.append("warm boots, and a scarf");
    } else {
        rec.append("and comfortable shoes");
    }

    return rec.toString();
}

private String getReasoningForRecommendation(double temp, String weatherDescription, int humidity) {
    StringBuilder reasoning = new StringBuilder("Based on the current weather conditions: ");
    
    reasoning.append(String.format("The temperature is %.1f°C. ", temp));
    if (temp < 10) {
        reasoning.append("It's quite cold, so warm clothing is essential to maintain body heat. ");
    } else if (temp < 20) {
        reasoning.append("It's cool, so some light layers are recommended for comfort. ");
    } else {
        reasoning.append("It's warm, so light, breathable clothing is ideal. ");
    }

    reasoning.append(String.format("The weather is described as '%s'. ", weatherDescription));
    if (weatherDescription.contains("rain")) {
        reasoning.append("Waterproof items are necessary to stay dry. ");
    } else if (weatherDescription.contains("snow")) {
        reasoning.append("Warm, waterproof footwear and additional insulation like scarves are important. ");
    } else if (weatherDescription.contains("clear")) {
        reasoning.append("No special weather protection is needed. ");
    }

    reasoning.append(String.format("The humidity is %d%%. ", humidity));
    if (humidity > 70) {
        reasoning.append("High humidity can make it feel warmer and increase sweating, so breathable fabrics are recommended.");
    } else if (humidity < 30) {
        reasoning.append("Low humidity can cause static electricity, so natural fibers might be more comfortable.");
    } else {
        reasoning.append("Humidity levels are comfortable, so standard clothing choices should be fine.");
    }

    return reasoning.toString();
}
}
