package ua.kpi.fict.carpark.util.factories;

import org.springframework.data.domain.Page;

import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.dto.response.BusDto;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Route;

public class BusFactory {

    public static Bus busInstance() {
        return Bus.builder()
            .model("Mercedes-Benz A305")
            .capacity(35)
            .build();
    }

    public static Page<Bus> busPageInstance() {
        return Page.empty();
    }

    public static Page<BusDto> busDtosPageInstance() {
        return Page.empty();
    }

    public static Bus busWithoutRouteInstance() {
        return Bus.builder().route(new Route()).build();
    }

    public static CreateBusDto createBusDtoInstance() {
        return CreateBusDto.builder()
            .model("VAZ A305")
            .capacity(35)
            .build();
    }

    public static CreateBusDto createBusDtoWithoutModelInstance() {
        return CreateBusDto.builder()
            .capacity(35)
            .build();
    }

    public static CreateBusDto createBusDtoWithIncorrectModelInstance() {
        return CreateBusDto.builder()
            .model("VAZA305...")
            .capacity(35)
            .build();
    }

    public static CreateBusDto createBusDtoWithoutCapacityInstance() {
        return CreateBusDto.builder()
            .model("VAZ A305")
            .build();
    }

    public static CreateBusDto createBusDtoWithTooLowCapacityInstance() {
        return CreateBusDto.builder()
            .model("VAZ A305")
            .capacity(9)
            .build();
    }

    public static CreateBusDto createBusDtoWithTooBigCapacityInstance() {
        return CreateBusDto.builder()
            .model("VAZ A305")
            .capacity(61)
            .build();
    }

    public static BusDto busDtoInstance() {
        return BusDto.builder()
            .id(1L)
            .build();
    }
}
