package com.btb.chalKak.domain.weather.controller;

import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_LOAD_WEATHER;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.comment.entity.Comment;
import com.btb.chalKak.domain.weather.service.WeatherService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

  private final WeatherService weatherService;
  private final ResponseService responseService;
  @GetMapping
  public ResponseEntity<?> getWeather(@RequestParam String lat, @RequestParam String lon) {

    CommonResponse<?> response = responseService.success(weatherService.getWeatherFromApi(lat, lon),  SUCCESS_LOAD_WEATHER);

    return ResponseEntity.ok(response);
  }
}
