package ru.naumov.restingweather.service;

import ru.naumov.restingweather.model.Weather;

public interface WeatherService {
    Weather getWeatherByCity(String city) throws Exception;
    boolean isCity(String city) throws Exception;
}
