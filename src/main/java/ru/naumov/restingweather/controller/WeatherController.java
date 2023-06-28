package ru.naumov.restingweather.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.naumov.restingweather.model.Weather;
import ru.naumov.restingweather.service.WeatherServiceAgent;

@RestController
@AllArgsConstructor
public class WeatherController {

    private final WeatherServiceAgent weatherServiceAgent;

    @GetMapping("/weather/openweather/{city}")
    public ResponseEntity<Weather> getOpenWeatherByCity(@PathVariable(name = "city") String city) throws Exception {
        return ResponseEntity.ok(weatherServiceAgent.getOpenWeather(city));
    }

    @GetMapping("/weather/weatherapi/{city}")
    public ResponseEntity<Weather> getWeatherApiByCity(@PathVariable(name = "city") String city) throws Exception {
        return ResponseEntity.ok(weatherServiceAgent.getWeatherApi(city));
    }

    @GetMapping("/weather/{city}")
    public ResponseEntity<Weather> getWeatherByCityDefault(@PathVariable(name = "city") String city) throws Exception {
        return ResponseEntity.ok(weatherServiceAgent.getOpenWeather(city));
    }
}
