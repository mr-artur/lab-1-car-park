package ua.kpi.fict.carpark.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.kpi.fict.carpark.dto.response.DriverDto;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;

public interface DriverService {

    void saveAsDriver(User user);

    Driver findById(long driverId);

    DriverDto findByUsername(String username);

    Page<DriverDto> findAll(Pageable pageable);

    Page<DriverDto> findAllWithoutBusAndOpenedAssignment(Pageable pageable);

    void unassignDriverFromBus(long driverId);
}
