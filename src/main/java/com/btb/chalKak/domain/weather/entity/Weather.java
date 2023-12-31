package com.btb.chalKak.domain.weather.entity;


import java.time.LocalDate;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weather_to_member")
public class Weather {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name ="weather_id")
  private Long id;
  @Column(name ="temp")
  private double temp; // 평균 기온
  @Column(name ="weather")
  private String weather; // 날씨
  @Column(name ="weather_icon")
  private String weatherIcon; // 날씨에 대한 아이콘 코드 (openWeather api , front와 연관 이미지 코드)
  @Column(name ="max_temp")
  private double maxTemp; // 최고 기온
  @Column(name ="min_temp")
  private double minTemp; // 최저 기온
  private LocalDate date;

  public void updateTemp(double temp) {
    this.temp = temp;
  }

  public void updateWeather(String weather) {
    this.weather = weather;
  }

  public void updateWeatherIcon(String weatherIcon) {
    this.weatherIcon = weatherIcon;
  }

  public void updateMaxTemp(double maxTemp) {
    this.maxTemp = maxTemp;
  }

  public void updateMinTemp(double minTemp) {
    this.minTemp = minTemp;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "geo_id")
  private AdministrativeGeoInfo administrativeGeoInfo;

//  @OneToOne(cascade = CascadeType.ALL)
//  private WeatherApi weatherApi;

}

