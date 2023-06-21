package ru.naumov.restingweather.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "weather")
public class Weather implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Transient
    private double temperature;

    @Override
    public String toString() {
        return "Температура в городе " + city + " : " + temperature + "°C";
    }
}
