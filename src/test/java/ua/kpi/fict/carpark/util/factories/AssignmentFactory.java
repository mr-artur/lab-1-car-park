package ua.kpi.fict.carpark.util.factories;

import org.springframework.data.domain.Page;

import ua.kpi.fict.carpark.dto.response.AssignmentDto;
import ua.kpi.fict.carpark.entity.Assignment;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;

public class AssignmentFactory {

    public static Assignment assignmentInstance() {
        return new Assignment();
    }

    public static AssignmentDto assignmentDtoInstance() {
        return AssignmentDto.builder()
            .driver(DriverFactory.driverDtoInstance())
            .bus(BusFactory.busDtoInstance())
            .initiator(UserFactory.userDtoInstance())
            .build();
    }

    public static Assignment assignmentWithRelationsInstance() {
        return Assignment.builder()
            .driver(DriverFactory.driverInstance())
            .bus(BusFactory.busInstance())
            .initiator(UserFactory.userInstance())
            .build();
    }

    public static Assignment assignmentInstance(Driver driver, Bus bus, User initiator) {
        return Assignment.builder()
            .driver(driver)
            .bus(bus)
            .initiator(initiator)
            .build();
    }

    public static Page<Assignment> assignmentPageInstance() {
        return Page.empty();
    }

    public static Page<AssignmentDto> assignmentDtosPageInstance() {
        return Page.empty();
    }
}
