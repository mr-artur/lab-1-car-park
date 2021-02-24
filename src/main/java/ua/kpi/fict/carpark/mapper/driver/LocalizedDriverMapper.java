package ua.kpi.fict.carpark.mapper.driver;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ua.kpi.fict.carpark.dto.response.DriverDto;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.mapper.LocalizedMapper;

@RequiredArgsConstructor
@Component
public class LocalizedDriverMapper extends LocalizedMapper<Driver, DriverDto> {

    private final DriverMapper mapper;

    @Override
    protected DriverDto toDtoWithDefaultData(Driver driver) {
        return mapper.toDriverDto(driver);
    }

    @Override
    protected DriverDto toDtoWithNativeData(Driver driver) {
        return mapper.toDriverDtoWithNativeData(driver);
    }
}
