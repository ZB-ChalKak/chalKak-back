package com.btb.chalKak.domain.weather.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDto {

    private LocalDate date;
    private String weather;
    private String icon;
    private double temperature;

}
