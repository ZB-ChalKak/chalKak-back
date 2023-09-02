 package com.btb.chalKak.domain.weather.service;

import com.btb.chalKak.domain.weather.entity.AdministrativeGeoInfo;
import com.btb.chalKak.domain.weather.repository.AdministrativeGeoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

 @Service
@RequiredArgsConstructor
@Slf4j
public class AdministrativeGeoService {

    private final AdministrativeGeoRepository administrativeGeoRepository;

    public List<AdministrativeGeoInfo> getAllDistricts() {
        return administrativeGeoRepository.findAll();
    }
}
