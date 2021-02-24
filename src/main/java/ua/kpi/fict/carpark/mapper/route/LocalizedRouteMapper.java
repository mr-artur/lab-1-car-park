package ua.kpi.fict.carpark.mapper.route;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.dto.response.RouteDto;
import ua.kpi.fict.carpark.entity.Route;
import ua.kpi.fict.carpark.mapper.LocalizedMapper;

@RequiredArgsConstructor
@Component
public class LocalizedRouteMapper extends LocalizedMapper<Route, RouteDto> {

    private final RouteMapper mapper;

    public Route toRoute(CreateRouteDto createRouteDto) {
        return mapper.toRoute(createRouteDto);
    }

    @Override
    protected RouteDto toDtoWithDefaultData(Route route) {
        return mapper.toRouteDto(route);
    }

    @Override
    protected RouteDto toDtoWithNativeData(Route route) {
        return mapper.toRouteDtoWithNativeData(route);
    }
}
