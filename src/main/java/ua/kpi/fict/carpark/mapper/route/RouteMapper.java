package ua.kpi.fict.carpark.mapper.route;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.dto.response.RouteDto;
import ua.kpi.fict.carpark.entity.Route;
import ua.kpi.fict.carpark.mapper.bus.BusMapper;

@Named("RouteMapper")
@Mapper(componentModel = "spring", uses = BusMapper.class)
public interface RouteMapper {

    Route toRoute(CreateRouteDto createRouteDto);

    @Mapping(target = "buses", qualifiedByName = {"BusMapper", "toBusDtoWithoutCyclingReferences"})
    RouteDto toRouteDto(Route route);

    @Mapping(target = "buses", qualifiedByName = {"BusMapper", "toBusDtoWithNativeDataWithoutCyclingReferences"})
    RouteDto toRouteDtoWithNativeData(Route route);

    @Named("toRouteDtoWithoutCyclingReferences")
    @Mapping(target = "buses", ignore = true)
    RouteDto toRouteDtoWithoutCyclingReferences(Route route);

    @Named("toRouteDtoWithNativeDataWithoutCyclingReferences")
    @Mapping(target = "buses", ignore = true)
    RouteDto toRouteDtoWithNativeDataWithoutCyclingReferences(Route route);
}
