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

import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.exception.BusNotFoundException;
import ua.kpi.fict.carpark.util.factories.BusFactory;
import ua.kpi.fict.carpark.util.factories.PageableFactory;
import ua.kpi.fict.carpark.mapper.bus.LocalizedBusMapper;
import ua.kpi.fict.carpark.repository.BusRepository;

@ExtendWith(MockitoExtension.class)
class BusServiceImplTest {

    @Mock
    private BusRepository busRepository;

    @Mock
    private LocalizedBusMapper mapper;

    @InjectMocks
    private BusServiceImpl busService;

    private final long correctId = 1L;
    private final Bus bus = BusFactory.busInstance();
    private final Pageable pageable = PageableFactory.pageableInstance();

    @Test
    void save_shouldCallSaveMethodOfBusRepository() {
        final CreateBusDto dto = BusFactory.createBusDtoInstance();
        when(mapper.toBus(dto)).thenReturn(bus);

        busService.save(dto);

        verify(busRepository).save(bus);
    }

    @Test
    void findById_shouldCallFindByIdMethodOfBusRepository_whenIdIsCorrect() {
        when(busRepository.findById(correctId)).thenReturn(Optional.of(bus));

        busService.findById(correctId);

        verify(busRepository).findById(correctId);
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenIdIsIncorrect() {
        final long incorrectId = -1L;
        when(busRepository.findById(incorrectId)).thenReturn(Optional.empty());

        try {
            busService.findById(incorrectId);
            fail("Exception was't thrown");
        } catch (Exception e) {
            assertEquals(BusNotFoundException.class, e.getClass());
            assertEquals(String.format("No bus found with such id : %s", incorrectId), e.getMessage());
        }
    }

    @Test
    void findAll_shouldCallFindAllMethodOfBusRepository() {
        when(busRepository.findAll(pageable)).thenReturn(BusFactory.busPageInstance());

        busService.findAll(pageable);

        verify(busRepository).findAll(pageable);
    }

    @Test
    void findAllWithoutRoute_shouldCallFindByRouteIsNullMethodOfBusRepository() {
        when(busRepository.findByRouteIsNull(pageable)).thenReturn(BusFactory.busPageInstance());

        busService.findAllWithoutRoute(pageable);

        verify(busRepository).findByRouteIsNull(pageable);
    }

    @Test
    void findAllWithoutDriverAndOpenedAssignment_shouldCallAppropriateMethodOfBusRepository() {
        when(busRepository.findByDriverIsNullAndAssignmentIsNull(pageable)).thenReturn(BusFactory.busPageInstance());

        busService.findAllWithoutDriverAndOpenedAssignment(pageable);

        verify(busRepository).findByDriverIsNullAndAssignmentIsNull(pageable);
    }

    @Test
    void assignBusToRoute_shouldCallAssignBusToRouteMethodOfBusRepository() {
        final long correctRouteId = 2L;

        busService.assignBusToRoute(correctId, correctRouteId);

        verify(busRepository).assignBusToRoute(correctId, correctRouteId);
    }

    @Test
    void unassignBusFromRoute_shoulCallUnassignBusFromRouteMethodOfBusRepository() {
        busService.unassignBusFromRoute(correctId);

        verify(busRepository).unassignBusFromRoute(correctId);
    }
}
