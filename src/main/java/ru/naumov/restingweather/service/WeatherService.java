package ru.naumov.restingweather.service;

import ru.naumov.restingweather.model.Weather;

public interface WeatherService {
    Weather getOpenWeatherByCity(String city) throws Exception;
    Weather getWeatherAlternativeByCity(String city) throws Exception;
    boolean isCity(String city) throws Exception;
}
