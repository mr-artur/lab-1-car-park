package ua.kpi.fict.carpark.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Pageable;

import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.exception.DriverNotFoundException;
import ua.kpi.fict.carpark.util.factories.DriverFactory;
import ua.kpi.fict.carpark.util.factories.PageableFactory;
import ua.kpi.fict.carpark.mapper.driver.LocalizedDriverMapper;
import ua.kpi.fict.carpark.repository.DriverRepository;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private LocalizedDriverMapper mapper;

    @InjectMocks
    private DriverServiceImpl driverService;

    private final long correctId = 1L;
    private final Driver driver = DriverFactory.driverInstance();
    private final Pageable pageable = PageableFactory.pageableInstance();

    @Test
    void saveAsDriver_shouldCallSaveMethodOfDriverRepository() {
        driverService.saveAsDriver(driver.getUser());

        verify(driverRepository).save(driver);
    }

    @Test
    void findById_shouldCallFindByIdMethodOfDriverRepository_whenIdIsCorrect() {
        when(driverRepository.findById(correctId)).thenReturn(Optional.of(driver));

        driverService.findById(correctId);

        verify(driverRepository).findById(correctId);
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenIdIsIncorrect() {
        final long incorrectId = -1L;
        when(driverRepository.findById(incorrectId)).thenReturn(Optional.empty());

        try {
            driverService.findById(incorrectId);
            fail("Exception was't thrown");
        } catch (Exception e) {
            assertEquals(DriverNotFoundException.class, e.getClass());
            assertEquals(String.format("No driver found with such id : %s", incorrectId), e.getMessage());
        }
    }

    @Test
    void findByUsername_shouldCallFindByUsernameMethodOfDriverRepository_whenUsernameIsCorrect() {
        final String correctUsername = "admin";
        when(driverRepository.findByUserUsername(correctUsername)).thenReturn(Optional.of(driver));
        when(mapper.toDto(driver)).thenReturn(DriverFactory.driverDtoInstance());

        driverService.findByUsername(correctUsername);

        verify(driverRepository).findByUserUsername(correctUsername);
    }

    @Test
    void findByUsername_shouldThrowEntityNotFoundException_whenUsernameIsIncorrect() {
        final String incorrectUsername = "something";
        when(driverRepository.findByUserUsername(incorrectUsername)).thenReturn(Optional.empty());

        try {
            driverService.findByUsername(incorrectUsername);
            fail("Exception was't thrown");
        } catch (Exception e) {
            assertEquals(DriverNotFoundException.class, e.getClass());
            assertEquals(String.format("No driver found with such username : %s", incorrectUsername),
                         e.getMessage());
        }
    }

    @Test
    void findAll_shouldCallFindAllMethodOfDriverRepository() {
        when(driverRepository.findAll(pageable)).thenReturn(DriverFactory.driverPageInstance());

        driverService.findAll(pageable);

        verify(driverRepository).findAll(pageable);
    }

    @Test
    void findAllWithoutBusAndOpenedAssignment_shouldCallAppropriateMethodOfDriverRepository() {
        when(driverRepository.findByBusIsNullAndAssignmentIsNull(pageable)).thenReturn(DriverFactory.driverPageInstance());

        driverService.findAllWithoutBusAndOpenedAssignment(pageable);

        verify(driverRepository).findByBusIsNullAndAssignmentIsNull(pageable);
    }

    @Test
    void unassignDriverFromBus_shouldCallUnassignDriverFromBusMethodOfDriverRepository() {
        driverService.unassignDriverFromBus(correctId);

        verify(driverRepository).unassignDriverFromBus(correctId);
    }
}
