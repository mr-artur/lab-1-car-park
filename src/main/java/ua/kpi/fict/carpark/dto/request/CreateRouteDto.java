package ua.kpi.fict.carpark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static ua.kpi.fict.carpark.TextConstants.ROUTE_NUMBER_MAX;
import static ua.kpi.fict.carpark.TextConstants.ROUTE_NUMBER_MIN;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateRouteDto {

    @NotNull
    @Min(ROUTE_NUMBER_MIN)
    @Max(ROUTE_NUMBER_MAX)
    private Integer number;
}
