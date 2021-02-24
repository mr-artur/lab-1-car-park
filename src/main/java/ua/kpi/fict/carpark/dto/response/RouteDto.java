package ua.kpi.fict.carpark.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDto {

    private Long id;

    private Integer number;

    private List<BusDto> buses = new ArrayList<>();
}
