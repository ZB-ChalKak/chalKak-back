package com.btb.chalKak.domain.weather.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Administrative_Geo_Info")
public class AdministrativeGeoInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name ="Geo_id")
  private Long id;
  @Column(name ="name")
  private String name; // 도시
  @Column(name ="longitude")
  private double lon; // longitude
  @Column(name ="latitude")
  private double lat; // latitude

}

