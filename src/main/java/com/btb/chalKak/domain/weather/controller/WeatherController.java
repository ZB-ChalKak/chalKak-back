package com.btb.chalKak.domain.weather.controller;

import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_WEATHER;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.weather.service.Impl.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

  private final WeatherServiceImpl weatherService;
  private final ResponseService responseService;
  @GetMapping
  public ResponseEntity<?> getWeather(@RequestParam String lat, @RequestParam String lon) {

    CommonResponse<?> response = responseService.success(weatherService.getWeather(lat, lon),  SUCCESS_LOAD_WEATHER);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/posts")
  public ResponseEntity<?> getWeatherPosts(
          Authentication authentication, @RequestParam String lat, @RequestParam String lon,
                                          Pageable pageable) {

    CommonResponse<?> response = responseService.success(weatherService.getWeatherPosts(authentication, lat, lon, pageable),  SUCCESS_LOAD_WEATHER);

    return ResponseEntity.ok(response);
  }
}
