package com.btb.chalKak.domain.weather.service;

import com.btb.chalKak.domain.weather.dto.WeatherDto;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherService {

  @Value("${openWeather.key}")
  private String apiKey;

  private String getWeatherString(String lat, String lon) {

//    String lat = "37.5683"; // 37.5683
//    String lon = "126.9778"; // 126.9778
//    http://localhost:8080/weather?lat=37.5683&lon=126.9778
    String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" +apiKey;

    try {
      URL url = new URL(apiUrl);

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      BufferedReader br;
      if (responseCode == 200) {
        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      } else {
        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
      }
      String inputLine;
      StringBuilder response = new StringBuilder();

      while ((inputLine = br.readLine()) != null) {
        response.append(inputLine);
      }
      br.close();

      return response.toString();
    } catch (Exception e) {
      return "failed to get response!";
    }

  }

  public WeatherDto getWeatherFromApi(String lat, String lon) {

    // open weather map에서 날씨 데이터 가져오기
    String weatherDate = getWeatherString(lat,lon);
    // 받아온 날씨 json 파싱
    Map<String, Object> parsedWeather = parseWeather(weatherDate);
    WeatherDto dateWeather = WeatherDto.builder()
        .date(LocalDate.ofInstant(Instant.ofEpochSecond((Long) parsedWeather.get("date")), ZoneId.systemDefault()))
        .weather(parsedWeather.get("main").toString())
        .temperature((Double) parsedWeather.get("temp"))
        .icon(parsedWeather.get("icon").toString())
        .build();

    return dateWeather;
  }

  private Map<String, Object> parseWeather(String jsonString) {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;

    try {

      jsonObject = (JSONObject) jsonParser.parse(jsonString);

    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    Map<String, Object> resultMap = new HashMap<>();
    JSONArray weatherList = (JSONArray)jsonObject.get("list");

    log.info("weather size" + weatherList.size());

    JSONObject weatherData = (JSONObject) weatherList.get(0);
    JSONObject mainData = (JSONObject) weatherData.get("main");
    Long dtData = (Long) weatherData.get("dt");
    String dataData = (String) weatherData.get("dt_txt");
    log.info(mainData.toJSONString());
    log.info("dataData" + dataData);
    log.info("dataData " + dtData);
    resultMap.put("date", dtData );
    resultMap.put("temp", mainData.get("temp"));

    JSONArray weatherArray = (JSONArray) weatherData.get("weather");
    JSONObject weaData = (JSONObject) weatherArray.get(0);
    resultMap.put("main", weaData.get("main"));
    resultMap.put("icon", weaData.get("icon"));

    return resultMap;

  }
}
