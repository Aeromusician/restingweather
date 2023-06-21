package ru.naumov.restingweather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import ru.naumov.restingweather.model.Weather;
import ru.naumov.restingweather.repository.WeatherRepository;

import java.net.URI;

@Service
public class WeatherServiceImpl implements WeatherService{

    private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid={key}&units=metric&lang=ru";

    @Value("${api.openweathermap.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final WeatherRepository repository;

    @Autowired
    public WeatherServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper, WeatherRepository repository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    public Weather getWeatherByCity(String city) {
        URI url = new UriTemplate(WEATHER_URL).expand(city, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return repository.save(convert(response));
    }


    private Weather convert(ResponseEntity<String> response) {
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            Weather weather = new Weather();
            weather.setCity(root.path("name").asText());
            weather.setTemperature(root.path("main").path("temp").asDouble());
            weather.setLongitude(root.path("coord").path("lon").asDouble());
            weather.setLatitude(root.path("coord").path("lat").asDouble());
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }
}
