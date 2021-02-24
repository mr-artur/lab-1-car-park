package ua.kpi.fict.carpark.mapper.assignment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ua.kpi.fict.carpark.dto.response.AssignmentDto;
import ua.kpi.fict.carpark.entity.Assignment;
import ua.kpi.fict.carpark.mapper.bus.BusMapper;
import ua.kpi.fict.carpark.mapper.driver.DriverMapper;
import ua.kpi.fict.carpark.mapper.user.UserMapper;

@Named("AssignmentMapper")
@Mapper(componentModel = "spring", uses = {UserMapper.class, BusMapper.class, DriverMapper.class})
public interface AssignmentMapper {

    @Mapping(target = "initiator", qualifiedByName = {"UserMapper", "toUserDto"})
    @Mapping(target = "driver", qualifiedByName = {"DriverMapper", "toDriverDtoWithoutCyclingReferences"})
    @Mapping(target = "bus", qualifiedByName = {"BusMapper", "toBusDtoWithoutCyclingReferences"})
    AssignmentDto toAssignmentDto(Assignment assignment);

    @Mapping(target = "initiator", qualifiedByName = {"UserMapper", "toUserDtoWithNativeData"})
    @Mapping(target = "driver", qualifiedByName = {"DriverMapper", "toDriverDtoWithNativeDataWithoutCyclingReferences"})
    @Mapping(target = "bus", qualifiedByName = {"BusMapper", "toBusDtoWithNativeDataWithoutCyclingReferences"})
    AssignmentDto toAssignmentDtoWithNativeData(Assignment assignment);

    @Named("toAssignmentDtoWithoutCyclingReferences")
    @Mapping(target = "initiator", qualifiedByName = {"UserMapper", "toUserDto"})
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "bus", ignore = true)
    AssignmentDto toAssignmentDtoWithoutCyclingReferences(Assignment assignment);

    @Named("toAssignmentDtoWithNativeDataWithoutCyclingReferences")
    @Mapping(target = "initiator", qualifiedByName = {"UserMapper", "toUserDtoWithNativeData"})
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "bus", ignore = true)
    AssignmentDto toAssignmentDtoWithNativeDataWithoutCyclingReferences(Assignment assignment);
}
