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
public class AssignmentDto {

    private Long id;

    private DriverDto driver;

    private BusDto bus;

    private UserDto initiator;
}
