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
    @SequenceGenerator(name = "weather_seq",
            sequenceName = "weather_seq",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_seq")
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "city_id")
    private City city;

//    @Column(name = "latitude")
//    private double latitude;
//
//    @Column(name = "longitude")
//    private double longitude;

    @Column(name = "temperature")
    private double temperature;

    @Override
    public String toString() {
        return "Температура в городе " + city.getName() + " : " + temperature + "°C";
    }
}
