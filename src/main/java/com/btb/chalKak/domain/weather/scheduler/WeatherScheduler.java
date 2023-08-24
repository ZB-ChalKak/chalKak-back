package com.btb.chalKak.domain.weather.scheduler;

import com.btb.chalKak.domain.weather.dto.WeatherDto;
import com.btb.chalKak.domain.weather.repository.WeatherRepository;
import com.btb.chalKak.domain.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeatherScheduler {

    private final WeatherRepository weatherRepository;

    private final WeatherService weatherService;

    @Transactional
    @Scheduled(cron = "${scheduler.weather.cron}")
    public void createNewWeatherForMember() {


        String lat = "";
        String lon = "";
        // 1. 시도 중요 지점의 위도와 경도를 가져온다.
        // 2. weather 서비스로 5일치 3시간 간격의 weather data를 가져온다.
        WeatherDto weatherDto = weatherService.getWeatherFromApi(lat,lon);
        // 3. 1일치의 칼럼으로 만든다.
        // 3-1. 평균 기온 , 최고 기온, 최저 기온,
        // 4. 5일치의 칼럼을 만들어 repository 저장한다.

    }

    private long getPostIdByRedisKey(String postViewCountKey) {
        return Long.parseLong(postViewCountKey.split("::")[1]);
    }
}
