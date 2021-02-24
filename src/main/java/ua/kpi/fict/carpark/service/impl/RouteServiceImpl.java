package ua.kpi.fict.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.dto.response.RouteDto;
import ua.kpi.fict.carpark.entity.Route;
import ua.kpi.fict.carpark.mapper.route.LocalizedRouteMapper;
import ua.kpi.fict.carpark.repository.RouteRepository;
import ua.kpi.fict.carpark.service.RouteService;

@RequiredArgsConstructor
@Log4j2
@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final LocalizedRouteMapper mapper;

    @Override
    public void save(CreateRouteDto createRouteDto) {
        log.debug("Request to save route : {}", createRouteDto);
        Route route = mapper.toRoute(createRouteDto);
        routeRepository.save(route);
        log.debug("Saved route : {}", route);
    }

    @Override
    public Page<RouteDto> findAllSorted(Pageable pageable) {
        return routeRepository.findAllByOrderByNumber(pageable).map(mapper::toDto);
    }
}
