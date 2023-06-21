package ru.naumov.restingweather.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.naumov.restingweather.model.Weather;
import ru.naumov.restingweather.service.WeatherServiceImpl;

@RestController
@AllArgsConstructor
public class WeatherController {

    private final WeatherServiceImpl weatherService;

    @GetMapping("/weather/{city}")
    public String getWeatherByCity(@PathVariable(name = "city") String city) {
        return weatherService.getWeatherByCity(city).toString();
    }
}
