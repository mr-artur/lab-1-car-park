package ua.kpi.fict.carpark.mapper.driver;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ua.kpi.fict.carpark.dto.response.DriverDto;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.mapper.user.UserMapper;
import ua.kpi.fict.carpark.mapper.assignment.AssignmentMapper;
import ua.kpi.fict.carpark.mapper.bus.BusMapper;

@Named("DriverMapper")
@Mapper(componentModel = "spring", uses = {UserMapper.class, BusMapper.class, AssignmentMapper.class})
public interface DriverMapper {

    @Mapping(target = "user", qualifiedByName = {"UserMapper", "toUserDto"})
    @Mapping(target = "bus", qualifiedByName = {"BusMapper", "toBusDtoWithoutCyclingReferences"})
    @Mapping(target = "assignment", qualifiedByName = {"AssignmentMapper", "toAssignmentDtoWithoutCyclingReferences"})
    DriverDto toDriverDto(Driver driver);

    @Mapping(
        target = "user",
        qualifiedByName = {"UserMapper", "toUserDtoWithNativeData"}
    )
    @Mapping(
        target = "bus",
        qualifiedByName = {"BusMapper", "toBusDtoWithNativeDataWithoutCyclingReferences"}
    )
    @Mapping(
        target = "assignment",
        qualifiedByName = {"AssignmentMapper", "toAssignmentDtoWithNativeDataWithoutCyclingReferences"}
    )
    DriverDto toDriverDtoWithNativeData(Driver driver);

    @Named("toDriverDtoWithoutCyclingReferences")
    @Mapping(target = "user", qualifiedByName = {"UserMapper", "toUserDto"})
    @Mapping(target = "bus", ignore = true)
    @Mapping(target = "assignment", ignore = true)
    DriverDto toDriverDtoWithoutCyclingReferences(Driver driver);

    @Named("toDriverDtoWithNativeDataWithoutCyclingReferences")
    @Mapping(target = "user", qualifiedByName = {"UserMapper", "toUserDtoWithNativeData"})
    @Mapping(target = "bus", ignore = true)
    @Mapping(target = "assignment", ignore = true)
    DriverDto toDriverDtoWithNativeDataWithoutCyclingReferences(Driver driver);
}
