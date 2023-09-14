package com.btb.chalKak.scheduler;

import com.btb.chalKak.domain.weather.dto.WeatherDto;
import com.btb.chalKak.domain.weather.entity.AdministrativeGeoInfo;
import com.btb.chalKak.domain.weather.entity.Weather;
import com.btb.chalKak.domain.weather.repository.WeatherRepository;
import com.btb.chalKak.domain.weather.service.AdministrativeGeoService;
import com.btb.chalKak.domain.weather.service.Impl.WeatherServiceImpl;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherScheduler {

    private final DataSource dataSource;

    private final WeatherRepository weatherRepository;
    private final WeatherServiceImpl weatherService;
    private List<AdministrativeGeoInfo> administrativeGeoInfos;
    private final AdministrativeGeoService administrativeGeoService;

    private int currentIndex = 0;
    private int runCount = 0;

    private final String defaultWeather = "Clear";
    private final String defaultIcon = "01d";

    @PostConstruct
    public void init() {
        log.debug("weather 스프링");
        administrativeGeoInfos = administrativeGeoService.getAllDistricts();
//        processGetWeather(); // 스프링이 올라오면서 저장함
    }

    public void deleteDataFromTable() {
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM weather_to_member");
        } catch (Exception e) {
            log.debug(e.getMessage());
            // 예외 처리
        }
    }

    @Transactional
    @Scheduled(cron = "${scheduler.weather.cron}")
    // 1시간 마다 스케쥴( 저장되어 모든 위도 경도에 따라 값을 변환하고 저장)
    // api call이 1분에 60개 최대이기 때문에, 임의로 50개씩 나누어 스케쥴을 돌려야함.(무료 티어 한계)
    // 현재 중요 거점 위도 경도가 300개 이하임
    public void processGetWeather() {
        log.info("06:00 weather 스케쥴 시작");
        deleteDataFromTable();

        for (int i = currentIndex; i < administrativeGeoInfos.size(); i++) {
            AdministrativeGeoInfo administrativeGeoInfo = administrativeGeoInfos.get(i);


            // 1. 시도 중요 지점의 위도와 경도를 가져온다.
            String lat = Double.toString(administrativeGeoInfo.getLat());
            String lon = Double.toString(administrativeGeoInfo.getLon());

            // 2. weather 서비스로 6일치 3시간 간격의 weather data를 가져온다.
            List<WeatherDto> weatherDto = weatherService.getWeatherFromApi(lat, lon);
            // 3. 1일치를 하나의 칼럼으로 만든다.
            // 3-1. 평균 기온 , 최고 기온, 최저 기온,
            List<Weather> weathers = convertDtosToWeathers(weatherDto, administrativeGeoInfo);

            weatherRepository.saveAll(weathers);

        }
        log.info("06:00 weather 스케쥴 종료");
    }

    private List<Weather> convertDtosToWeathers(List<WeatherDto> weatherDtos,AdministrativeGeoInfo administrativeGeoInfo) {

        Map<LocalDate, List<WeatherDto>> weatherDtosByDate = weatherDtos.stream()
                .collect(Collectors.groupingBy(WeatherDto::getDate));

        List<Weather> weathers = new ArrayList<>();


        for (Map.Entry<LocalDate, List<WeatherDto>> entry : weatherDtosByDate.entrySet()) {
            LocalDate date = entry.getKey();
            double minTemp = Double.MAX_VALUE;
            double maxTemp = Double.MIN_VALUE;
            double averageTemp = 0.0;
            Map<String, String> weatherFrequency = new HashMap<>();
            Map<String, String> iconFrequency = new HashMap<>();

            List<WeatherDto> dtosForDate = entry.getValue();
            int cur = 0;
            for (WeatherDto weatherDto : dtosForDate) {
                double temp = weatherDto.getTemperature();
                averageTemp += temp;
                maxTemp = Double.max(maxTemp, temp);
                minTemp = Double.min(minTemp, temp);
                weatherFrequency.put(""+cur,weatherDto.getWeather());
                iconFrequency.put(""+cur,weatherDto.getIcon());
                cur++;
            }

            averageTemp /= dtosForDate.size();

            String mostFrequentWeather = weatherFrequency.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getValue)
                    .orElse(defaultWeather);

            String mostFrequentIcon = iconFrequency.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getValue)
                    .orElse(defaultIcon);

            // Creating a new Weather object using the calculated values
            weathers.add(Weather.builder()
                    .temp(averageTemp)
                    .maxTemp(maxTemp)
                    .weatherIcon(mostFrequentIcon)
                    .weather(mostFrequentWeather)
                            .administrativeGeoInfo(administrativeGeoInfo)
                    .minTemp(minTemp)
                    .date(date)
                    .build());
        }

        return weathers;
    }
}
