package com.btb.chalKak.domain.weather.entity;


import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weatherTomember")
public class Weather {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private double temp; // 평균 기온
  private String weather; // 날씨
  private String weatherIcon; // 날씨에 대한 아이콘 코드 (openWeather api , front와 연관 이미지 코드)
  private double maxTemp; // 최고 기온
  private double minTemp; // 최저 기온
  private LocalDate date;

//  @OneToOne(cascade = CascadeType.ALL)
//  private WeatherApi weatherApi;

}

