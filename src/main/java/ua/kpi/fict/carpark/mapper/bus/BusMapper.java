package ua.kpi.fict.carpark.mapper.bus;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.dto.response.BusDto;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.mapper.driver.DriverMapper;
import ua.kpi.fict.carpark.mapper.route.RouteMapper;
import ua.kpi.fict.carpark.mapper.assignment.AssignmentMapper;

@Named("BusMapper")
@Mapper(componentModel = "spring", uses = {DriverMapper.class, AssignmentMapper.class, RouteMapper.class})
public interface BusMapper {

    Bus toBus(CreateBusDto createBusDto);

    @Mapping(target = "driver", qualifiedByName = {"DriverMapper", "toDriverDtoWithoutCyclingReferences"})
    @Mapping(target = "assignment", qualifiedByName = {"AssignmentMapper", "toAssignmentDtoWithoutCyclingReferences"})
    @Mapping(target = "route", qualifiedByName = {"RouteMapper", "toRouteDtoWithoutCyclingReferences"})
    BusDto toBusDto(Bus bus);

    @Mapping(
        target = "driver",
        qualifiedByName = {"DriverMapper", "toDriverDtoWithNativeDataWithoutCyclingReferences"}
    )
    @Mapping(
        target = "assignment",
        qualifiedByName = {"AssignmentMapper", "toAssignmentDtoWithNativeDataWithoutCyclingReferences"}
    )
    @Mapping(
        target = "route",
        qualifiedByName = {"RouteMapper", "toRouteDtoWithNativeDataWithoutCyclingReferences"}
    )
    BusDto toBusDtoWithNativeData(Bus bus);

    @Named("toBusDtoWithoutCyclingReferences")
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "assignment", ignore = true)
    @Mapping(target = "route", qualifiedByName = {"RouteMapper", "toRouteDtoWithoutCyclingReferences"})
    BusDto toBusDtoWithoutCyclingReferences(Bus bus);

    @Named("toBusDtoWithNativeDataWithoutCyclingReferences")
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "assignment", ignore = true)
    @Mapping(target = "route", qualifiedByName = {"RouteMapper", "toRouteDtoWithoutCyclingReferences"})
    BusDto toBusDtoWithNativeDataWithoutCyclingReferences(Bus bus);
}
