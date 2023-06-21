package ru.naumov.restingweather.service;

import org.springframework.http.ResponseEntity;
import ru.naumov.restingweather.model.Weather;

public interface WeatherService {
    Weather getWeatherByCity(String city);
}
