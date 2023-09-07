package com.btb.chalKak.domain.weather.service.Impl;

import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.domain.like.dto.LikerResponse;
import com.btb.chalKak.domain.like.dto.LoadPageLikeResponse;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.service.Impl.MemberServiceImpl;
import com.btb.chalKak.domain.post.dto.response.LoadPublicPostsResponse;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import com.btb.chalKak.domain.weather.dto.WeatherDto;
import com.btb.chalKak.domain.weather.entity.Weather;
import com.btb.chalKak.domain.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOUND_STYLETAG_KEYWORD;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherServiceImpl {

    @Value("${openWeather.key}")
    private String apiKey;

    private final WeatherRepository weatherRepository;
    private final StyleTagRepository styleTagRepository;

    private final PostRepository postRepository;

    private final MemberServiceImpl memberService;


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

        String apiUrl = "http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;

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
            resultMap.put("date", dtData.toString());
            resultMap.put("temp", mainData.get("temp").toString());

            JSONArray weatherArray = (JSONArray) weatherData.get("weather");
            JSONObject weaData = (JSONObject) weatherArray.get(0);
            resultMap.put("main", weaData.get("main").toString());
            resultMap.put("icon", weaData.get("icon").toString());

            resultMaps.add(resultMap);
        }


        return resultMaps;

    }

    public LoadPublicPostsResponse getWeatherPosts(Authentication authentication, String lat, String lon, Pageable pageable) {

        // 1. lan, lon 으로 근처 시도의 당일 평균 날씨
        // lat : 37.74913611, lon : 128.8784972
        LocalDate today = LocalDate.now();

        Weather weather = weatherRepository.findClosestWeatherByLatLonAndDate(Double.parseDouble(lat),Double.parseDouble(lon), today);

        // 2. 평균 날씨의 쾌창함 정도(sunny, rainy, snow 등) AND member의 style_tag로 post 구분
        Member member = memberService.getMemberByAuthentication(authentication);

        List<Long> styleTagIds = Optional.ofNullable(member.getStyleTags())
                .orElse(Collections.emptyList())
                .stream().map(x -> x.getId())
                .collect(Collectors.toList()); // member의 style 태그

        // 3. 평균 날씨의 Date, 온도로 봄, 여름 , 가을, 겨울 구분
        Long weatherId = weatherToStyleTagId(weather.getWeather());  // 맑음(Clear), 비(Rainy) , 흐림(Clouds) , 눈
        Long seasonId = weatherToSeasonId(weather.getDate(), weather.getTemp()); // 봄,여름,가을,겨울


        // 4. post에서 pageable 처리 ( view count  + like count 높은 순)
        Page<Post> posts = postRepository.findPostsByStyleTagsAndWeatherIdAndSeasonId(styleTagIds,weatherId,seasonId,pageable);

        LoadPublicPostsResponse data = LoadPublicPostsResponse.fromPage(posts);


        return data;
    }

    private Long weatherToStyleTagId (String weather){
        Map<String, String> styleMap = new HashMap<>();

        styleMap.put("Clear","맑음");
        styleMap.put("Clouds","흐림");
        styleMap.put("Rain","비");
        styleMap.put("Snow","눈");

        log.info(styleMap.get(weather));

        StyleTag styleTag =  styleTagRepository.findByKeyword(styleMap.get(weather))
                .orElseThrow(()->new PostException(NOT_FOUND_STYLETAG_KEYWORD));

        return styleTag.getId();
    }

    private Long weatherToSeasonId (LocalDate localDate, double temperature){

        String season = getSeason(localDate);
        temperature = fahrenheitToCelsius(temperature);

        log.info(season);

        StyleTag styleTag =  styleTagRepository.findByKeyword(season)
                .orElseThrow(()->new PostException(NOT_FOUND_STYLETAG_KEYWORD));

        return styleTag.getId();
    }
    public static double fahrenheitToCelsius(double fahrenheit) {
            return (fahrenheit - 32) / 1.8;
    }

    public static String getSeason(LocalDate date) {
        Month month = date.getMonth();

        switch (month) {
            case MARCH:
            case APRIL:
            case MAY:
                return "봄";
            case JUNE:
            case JULY:
            case AUGUST:
                return "여름";
            case SEPTEMBER:
            case OCTOBER:
            case NOVEMBER:
                return "가을";
            case DECEMBER:
            case JANUARY:
            case FEBRUARY:
            default:
                return "겨울";
        }
    }
}
