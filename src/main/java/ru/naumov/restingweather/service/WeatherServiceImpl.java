package ru.naumov.restingweather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import ru.naumov.restingweather.model.Weather;
import ru.naumov.restingweather.repository.WeatherRepository;

import java.net.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${api.openweathermap.url}")
    private String OPEN_WEATHER_URL;

    @Value("${api.openweathermap.key}")
    private String OPEN_WEATHER_KEY;

    @Value("${api.weatherapi.url}")
    private String WEATHER_API_URL;

    @Value("${api.weatherapi.key}")
    private String WEATHER_API_KEY;


    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final WeatherRepository repository;

    @Autowired
    public WeatherServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper, WeatherRepository repository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    public Weather getOpenWeatherByCity(String city) throws Exception {
        if (isCity(city)) {
            URI url = new UriTemplate(OPEN_WEATHER_URL).expand(city, OPEN_WEATHER_KEY);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return repository.save(convertOpenWeather(response));
        } else {
            throw new HttpMessageNotReadableException("City not exist");
        }
    }

    public Weather getWeatherAlternativeByCity(String city) throws Exception {
        if (isCity(city)) {
            URI url = new UriTemplate(WEATHER_API_URL).expand(WEATHER_API_KEY, city);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return repository.save(convertWeatherApi(response));

        }else {
            throw new HttpMessageNotReadableException("City not exist");
        }
    }

    private Weather convertOpenWeather(ResponseEntity<String> response) {
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

    private Weather convertWeatherApi(ResponseEntity<String> response) {
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            Weather weather = new Weather();
            weather.setCity(root.path("location").path("name").asText());
            weather.setLongitude(root.path("location").path("lon").asDouble());
            weather.setLatitude(root.path("location").path("lat").asDouble());
            weather.setTemperature(root.path("current").path("temp_c").asDouble());
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    public boolean isCity(String city) throws Exception {
        URL weatherApiUrl = new URL(OPEN_WEATHER_URL.replace("{city}", city).replace("{key}", OPEN_WEATHER_KEY));
        HttpURLConnection weatherApiConnection = (HttpURLConnection) weatherApiUrl.openConnection();
        weatherApiConnection.setRequestMethod("GET");
        weatherApiConnection.connect();
        return weatherApiConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

}
