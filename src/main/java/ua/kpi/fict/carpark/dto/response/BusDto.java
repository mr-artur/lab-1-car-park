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
public class BusDto {

    private Long id;

    private String model;

    private Integer capacity;

    private RouteDto route;

    private DriverDto driver;

    private AssignmentDto assignment;
}
