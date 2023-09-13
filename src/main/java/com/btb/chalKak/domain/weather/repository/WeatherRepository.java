package com.btb.chalKak.domain.weather.repository;

import com.btb.chalKak.domain.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<Weather> findByAdministrativeGeoInfo_IdAndDate(Long geoId, LocalDate date);

    @Query(value = "SELECT w.* FROM weather_to_member w " +
            "JOIN administrative_geo_info a ON w.geo_id = a.geo_id " +
            "WHERE w.date = :date " +
            "ORDER BY " +
            "ST_Distance_Sphere(point(a.longitude, a.latitude), point(:longitude, :latitude)) " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Weather> findClosestWeatherByLatLonAndDate(@Param("latitude") double latitude,
                                              @Param("longitude") double longitude,
                                              @Param("date") String date);
}
