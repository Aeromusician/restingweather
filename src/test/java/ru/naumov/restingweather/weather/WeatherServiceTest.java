package ru.naumov.restingweather.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import ru.naumov.restingweather.model.Weather;
import ru.naumov.restingweather.repository.WeatherRepository;
import ru.naumov.restingweather.service.WeatherServiceImpl;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@PropertySource("classpath:application.properties")
//@TestPropertySource("classpath:application.properties")
public class WeatherServiceTest {

    private static final String OPEN_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid={key}&units=metric&lang=ru";

    private static final String OPEN_WEATHER_KEY = "7c2848e54850add3f6899fab1269d029";

    private static final String WEATHER_API_URL = "http://api.weatherapi.com/v1/current.json?key={key}&q={city}&lang=ru";

    private static final String WEATHER_API_KEY = "5caa8035afba4011bfc94235232606";


    //private static final String CONNECT_URL_OPEN_WEATHER="http://api.openweathermap.org/data/2.5/weather?q=Москва&appid=7c2848e54850add3f6899fab1269d029&units=metric&lang=ru";
    //private static final String CONNECT_URL_WEATHER_API="http://api.weatherapi.com/v1/current.json?key=5caa8035afba4011bfc94235232606&q=Москва&lang=ru";
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WeatherRepository repository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    public void GetOpenWeatherByCityTest() throws Exception {
        String city = "Moscow";
        URI url = new UriTemplate(OPEN_WEATHER_URL).expand(city, OPEN_WEATHER_KEY);
        //URI url = new URI(CONNECT_URL_OPEN_WEATHER);
        String responseBody = "{\"name\":\"Москва\",\"coord\":{\"lat\":55.75,\"lon\":37.62},\"main\":{\"temp\":16.0}}";
        ResponseEntity<String> response = new ResponseEntity<>(responseBody, HttpStatus.OK);
        Weather expectedWeather = new Weather();
        expectedWeather.setCity("Москва");
        expectedWeather.setLatitude(55.75);
        expectedWeather.setLongitude(37.62);
        expectedWeather.setTemperature(16);
        when(restTemplate.getForEntity(url, String.class)).thenReturn(response);
        when(repository.save(any(Weather.class))).thenReturn(expectedWeather);
        when(objectMapper.readTree(responseBody)).thenReturn(getOpenWeatherJsonNode());

        Weather actualWeather = weatherService.getOpenWeatherByCity(city);

        assertEquals(expectedWeather, actualWeather);
    }

    @Test
    public void GetOpenWeatherByCityCityNotExistTest() throws Exception {
        String city = "Qwerty";
        URI url = new UriTemplate(OPEN_WEATHER_URL).expand(city, OPEN_WEATHER_KEY);
        String responseBody = "{\"cod\":\"404\",\"message\":\"city not found\"}";
        ResponseEntity<String> response = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(response);

        Assertions.assertThrows(HttpMessageNotReadableException.class, () -> weatherService.getOpenWeatherByCity(city));
    }

    @Test
    public void GetWeatherAlternativeByCityTest() throws Exception {
        String city = "Moscow";
        URI url = new UriTemplate(WEATHER_API_URL).expand(WEATHER_API_KEY, city);
        String responseBody = "{\"location\":{\"name\":\"Moscow\",\"lat\":55.75,\"lon\":37.62,\"tz_id\":\"Europe/Moscow\"},\"current\":{\"temp_c\":16.0}}";
        ResponseEntity<String> response = new ResponseEntity<>(responseBody, HttpStatus.OK);
        Weather expectedWeather = new Weather();
        expectedWeather.setCity("Москва");
        expectedWeather.setLatitude(55.75);
        expectedWeather.setLongitude(37.62);
        expectedWeather.setTemperature(16);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(response);
        when(repository.save(any(Weather.class))).thenReturn(expectedWeather);
        when(objectMapper.readTree(responseBody)).thenReturn(getWeatherApiJsonNode());

        Weather actualWeather = weatherService.getWeatherAlternativeByCity(city);

        assertEquals(expectedWeather, actualWeather);
    }

    @Test
    public void GetWeatherAlternativeByCityCityNotExistTest() throws Exception {
        String city = "Qwerty";
        URI url = new UriTemplate(WEATHER_API_URL).expand(WEATHER_API_KEY, city);
        String responseBody = "{\"error\":{\"code\":1006,\"message\":\"No matching location found.\"}}";
        ResponseEntity<String> response = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(response);

        Assertions.assertThrows(HttpMessageNotReadableException.class, () -> weatherService.getWeatherAlternativeByCity(city));
    }

    private JsonNode getOpenWeatherJsonNode() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "{\"name\":\"Moscow\",\"coord\":{\"lat\":55.75,\"lon\":37.62},\"main\":{\"temp\":16.0}}";
        return objectMapper.readTree(jsonString);
    }

    private JsonNode getWeatherApiJsonNode() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "{\"location\":{\"name\":\"Moscow\",\"lat\":55.7522,\"lon\":37.6156,\"tz_id\":\"Europe/Moscow\"},\"current\":{\"temp_c\":15.0}}";
        return objectMapper.readTree(jsonString);
    }

}
