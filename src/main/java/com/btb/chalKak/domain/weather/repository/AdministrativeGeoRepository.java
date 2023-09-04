package com.btb.chalKak.domain.weather.repository;

import com.btb.chalKak.domain.weather.entity.AdministrativeGeoInfo;
import com.btb.chalKak.domain.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrativeGeoRepository extends JpaRepository<AdministrativeGeoInfo, Long> {
}
