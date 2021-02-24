package ua.kpi.fict.carpark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {

    private Long id;

    private UserDto user;

    private AssignmentDto assignment;

    private BusDto bus;
}
