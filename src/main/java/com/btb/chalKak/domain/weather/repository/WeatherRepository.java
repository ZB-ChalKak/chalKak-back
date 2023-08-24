package com.btb.chalKak.domain.weather.repository;

import com.btb.chalKak.domain.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

}
