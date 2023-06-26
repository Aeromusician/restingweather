package ru.naumov.restingweather.weather;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import ru.naumov.restingweather.repository.WeatherRepository;
import ru.naumov.restingweather.service.WeatherServiceImpl;



@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private WeatherServiceImpl weatherService;

}
