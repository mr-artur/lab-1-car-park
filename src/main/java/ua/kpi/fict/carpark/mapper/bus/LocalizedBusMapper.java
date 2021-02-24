package ua.kpi.fict.carpark.mapper.bus;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.dto.response.BusDto;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.mapper.LocalizedMapper;

@RequiredArgsConstructor
@Component
public class LocalizedBusMapper extends LocalizedMapper<Bus, BusDto> {

    private final BusMapper mapper;

    public Bus toBus(CreateBusDto createBusDto) {
        return mapper.toBus(createBusDto);
    }

    @Override
    protected BusDto toDtoWithDefaultData(Bus bus) {
        return mapper.toBusDto(bus);
    }

    @Override
    protected BusDto toDtoWithNativeData(Bus bus) {
        return mapper.toBusDtoWithNativeData(bus);
    }
}
