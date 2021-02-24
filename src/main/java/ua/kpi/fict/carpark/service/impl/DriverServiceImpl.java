package ua.kpi.fict.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.kpi.fict.carpark.dto.response.DriverDto;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.exception.DriverNotFoundException;
import ua.kpi.fict.carpark.mapper.driver.LocalizedDriverMapper;
import ua.kpi.fict.carpark.repository.DriverRepository;
import ua.kpi.fict.carpark.service.DriverService;

@RequiredArgsConstructor
@Log4j2
@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final LocalizedDriverMapper mapper;

    @Override
    public void saveAsDriver(User user) {
        log.debug("Request to save user as driver : {}", user);
        Driver driver = Driver.builder().user(user).build();
        driverRepository.save(driver);
        log.debug("Saved driver : {}", driver);
    }

    @Override
    public Driver findById(long driverId) {
        return driverRepository.findById(driverId)
            .orElseThrow(() -> new DriverNotFoundException(String.format("No driver found with such id : %s",
                                                                         driverId)));
    }

    @Override
    public DriverDto findByUsername(String username) {
        return driverRepository.findByUserUsername(username).map(mapper::toDto)
            .orElseThrow(() -> new DriverNotFoundException(String.format("No driver found with such username : %s",
                                                                         username)));
    }

    @Override
    public Page<DriverDto> findAll(Pageable pageable) {
        return driverRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<DriverDto> findAllWithoutBusAndOpenedAssignment(Pageable pageable) {
        return driverRepository.findByBusIsNullAndAssignmentIsNull(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void unassignDriverFromBus(long driverId) {
        log.debug("Request to unassign driver with id : {} from bus", driverId);
        driverRepository.unassignDriverFromBus(driverId);
        log.debug("Unassigned driver with id : {} from bus", driverId);
    }
}
