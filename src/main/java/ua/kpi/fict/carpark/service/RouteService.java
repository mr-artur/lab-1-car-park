package ua.kpi.fict.carpark.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.dto.response.RouteDto;

public interface RouteService {

    void save(CreateRouteDto createRouteDto);

    Page<RouteDto> findAllSorted(Pageable pageable);
}
