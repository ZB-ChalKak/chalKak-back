package com.btb.chalKak.domain.weather.service;

import com.btb.chalKak.domain.weather.dto.WeatherDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherService {

  String getWeatherString(String lat, String lon);
  List<WeatherDto> getWeatherFromApi(String lat, String lon);
}
