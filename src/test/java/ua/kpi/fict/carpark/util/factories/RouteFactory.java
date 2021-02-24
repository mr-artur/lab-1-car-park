package ua.kpi.fict.carpark.util.factories;

import org.springframework.data.domain.Page;

import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.dto.response.RouteDto;
import ua.kpi.fict.carpark.entity.Route;

public class RouteFactory {

    public static CreateRouteDto createRouteDtoInstance() {
        return CreateRouteDto.builder().number(1).build();
    }

    public static Route routeInstance() {
        return Route.builder().number(1).build();
    }

    public static Page<Route> routePageInstance() {
        return Page.empty();
    }

    public static Page<RouteDto> routeDtosPageInstance() {
        return Page.empty();
    }

    public static CreateRouteDto createRouteDtoWithoutNumberInstance() {
        return CreateRouteDto.builder().build();
    }

    public static CreateRouteDto createRouteDtoWithTooLowNumberInstance() {
        return CreateRouteDto.builder().number(0).build();
    }

    public static CreateRouteDto createRouteDtoWithTooBigNumberInstance() {
        return CreateRouteDto.builder().number(1000).build();
    }
}
