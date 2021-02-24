package ua.kpi.fict.carpark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static ua.kpi.fict.carpark.TextConstants.BUS_CAPACITY_MAX;
import static ua.kpi.fict.carpark.TextConstants.BUS_CAPACITY_MIN;
import static ua.kpi.fict.carpark.TextConstants.REGEX_BUS_MODEL;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateBusDto {

    @NotNull
    @Pattern(regexp = REGEX_BUS_MODEL)
    private String model;

    @NotNull
    @Min(value = BUS_CAPACITY_MIN)
    @Max(value = BUS_CAPACITY_MAX)
    private Integer capacity;
}
