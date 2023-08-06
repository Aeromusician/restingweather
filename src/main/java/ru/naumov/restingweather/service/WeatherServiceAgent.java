package ru.naumov.restingweather.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.naumov.restingweather.model.Weather;

@Service
@AllArgsConstructor
public class WeatherServiceAgent {

    private OpenWeatherService openWeatherService;

    private WeatherApiService weatherApiService;


    public Weather getOpenWeather(String city) throws Exception {
        return openWeatherService.getWeatherByCity(city);
    }

    public Weather getWeatherApi(String city) throws Exception {
        return weatherApiService.getWeatherByCity(city);
    }

}
