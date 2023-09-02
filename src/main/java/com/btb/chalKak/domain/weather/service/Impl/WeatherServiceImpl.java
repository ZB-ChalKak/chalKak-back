package com.btb.chalKak.domain.weather.service.Impl;

import com.btb.chalKak.domain.weather.dto.WeatherDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class WeatherServiceImpl {

    @Value("${openWeather.key}")
    private String apiKey;

    /**
     * 사용자가 날씨를 조회할 때 자신의 날씨만 내려줌
     * @param lat
     * @param lon
     * @return
     */
    public WeatherDto getWeather(String lat, String lon) {

        // open weather map에서 날씨 데이터 가져오기
        String weatherDate = getWeatherString(lat, lon);
        // 받아온 날씨 json 파싱
        Map<String, String> parsedWeather = parseWeather(weatherDate);

        WeatherDto dateWeather = WeatherDto.builder()
                .date(LocalDate.ofInstant(Instant.ofEpochSecond(Long.parseLong(parsedWeather.get("date"))), ZoneId.of("Asia/Seoul")))
                .weather(parsedWeather.get("main").toString())
                .temperature(Double.parseDouble(parsedWeather.get("temp")))
                .icon(parsedWeather.get("icon").toString())
                .build();

        return dateWeather;
    }

    private String getWeatherString(String lat, String lon) {

//    String lat = "37.5683"; // 37.5683
//    String lon = "126.9778"; // 126.9778
//    http://localhost:8080/weather?lat=37.5683&lon=126.9778

        String apiUrl = "http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;

        log.info(apiUrl);

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

    /**
     * 스케쥴 할 때 어드민이 설정해둔 시,도의 중요 거점에 따른 날씨를 가져옴
     * @param lat
     * @param lon
     * @return List<WeatherDto>
     */
    public List<WeatherDto> getWeatherFromApi(String lat, String lon) {

        // open weather map에서 날씨 데이터 가져오기
        String weatherDate = getWeatherString(lat, lon);
        // 받아온 날씨 json 파싱
        List<Map<String, String>> parsedWeathers = parseWeathers(weatherDate);
        List<WeatherDto> weatherDtos = new ArrayList<>();

        for (Map<String, String> parsedWeather : parsedWeathers) {

            WeatherDto dateWeather = WeatherDto.builder()
                    .date(LocalDate.ofInstant(Instant.ofEpochSecond(Long.parseLong(parsedWeather.get("date"))), ZoneId.of("Asia/Seoul")))
                    .weather(parsedWeather.get("main").toString())
                    .temperature(Double.parseDouble(parsedWeather.get("temp")))
                    .icon(parsedWeather.get("icon").toString())
                    .build();

            weatherDtos.add(dateWeather);
        }
        return weatherDtos;
    }

    private Map<String, String> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        log.info(jsonString);

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONArray weatherList = (JSONArray) jsonObject.get("list");
        Map<String, String> resultMap = new HashMap<>();

        JSONObject weatherData = (JSONObject) weatherList.get(0);
        JSONObject mainData = (JSONObject) weatherData.get("main");
        Long dtData = (Long) weatherData.get("dt");
        log.info(mainData.toJSONString());
        log.info("dataData " + dtData);
        resultMap.put("date", dtData.toString());
        resultMap.put("temp", mainData.get("temp").toString());

        JSONArray weatherArray = (JSONArray) weatherData.get("weather");
        JSONObject weaData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weaData.get("main").toString());
        resultMap.put("icon", weaData.get("icon").toString());


        return resultMap;

    }

    private List<Map<String, String>> parseWeathers(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        log.info(jsonString);
        try {

            jsonObject = (JSONObject) jsonParser.parse(jsonString);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        log.debug(jsonObject.toJSONString());
        JSONArray weatherList = (JSONArray) jsonObject.get("list");

        List<Map<String, String>> resultMaps = new ArrayList<>();

        for (int i = 0; i < weatherList.size(); i++) {

            Map<String, String> resultMap = new HashMap<>();

            JSONObject weatherData = (JSONObject) weatherList.get(i);
            JSONObject mainData = (JSONObject) weatherData.get("main");
            Long dtData = (Long) weatherData.get("dt");
            log.info(mainData.toJSONString());
            log.info("dataData " + dtData);
            resultMap.put("date", dtData.toString());
            resultMap.put("temp", mainData.get("temp").toString());
            log.info(""+mainData.get("temp"));

            JSONArray weatherArray = (JSONArray) weatherData.get("weather");
            JSONObject weaData = (JSONObject) weatherArray.get(0);
            resultMap.put("main", weaData.get("main").toString());
            resultMap.put("icon", weaData.get("icon").toString());

            resultMaps.add(resultMap);
        }


        return resultMaps;

    }
}
