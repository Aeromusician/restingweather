package ru.naumov.restingweather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.naumov.restingweather.model.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

}
