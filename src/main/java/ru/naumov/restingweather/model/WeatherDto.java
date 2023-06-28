package ru.naumov.restingweather.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDto {

    private Long id;

    private String city;

    private double latitude;

    private double longitude;

    private double temperature;
}
