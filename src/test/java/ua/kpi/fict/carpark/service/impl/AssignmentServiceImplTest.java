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

import ua.kpi.fict.carpark.entity.Assignment;
import ua.kpi.fict.carpark.exception.AssignmentNotFoundException;
import ua.kpi.fict.carpark.util.factories.AssignmentFactory;
import ua.kpi.fict.carpark.util.factories.BusFactory;
import ua.kpi.fict.carpark.util.factories.DriverFactory;
import ua.kpi.fict.carpark.util.factories.PageableFactory;
import ua.kpi.fict.carpark.util.factories.UserFactory;
import ua.kpi.fict.carpark.mapper.assignment.LocalizedAssignmentMapper;
import ua.kpi.fict.carpark.repository.AssignmentRepository;
import ua.kpi.fict.carpark.service.BusService;
import ua.kpi.fict.carpark.service.DriverService;
import ua.kpi.fict.carpark.service.UserService;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceImplTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private BusService busService;

    @Mock
    private DriverService driverService;

    @Mock
    private UserService userService;

    @Mock
    private LocalizedAssignmentMapper mapper;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    private final long correctId = 1L;
    private final String correctUsername = "admin";
    private final Assignment assignment = AssignmentFactory.assignmentInstance();
    private final Assignment assignmentWithRelations = AssignmentFactory.assignmentWithRelationsInstance();

    @Test
    void findByDriverUsername_shouldCallFindByDriverUserUsernameMethodOfAssignmentRepository_whenUsernameIsCorrect() {
        when(assignmentRepository.findByDriverUserUsername(correctUsername)).thenReturn(Optional.of(assignment));
        when(mapper.toDto(assignment)).thenReturn(AssignmentFactory.assignmentDtoInstance());

        assignmentService.findByDriverUsername(correctUsername);

        verify(assignmentRepository).findByDriverUserUsername(correctUsername);
    }

    @Test
    void findByDriverUsername_shouldThrowEntityNotFoundException_whenUsernameIsIncorrect() {
        final String incorrectUsername = "something";
        when(assignmentRepository.findByDriverUserUsername(incorrectUsername)).thenReturn(Optional.empty());

        try {
            assignmentService.findByDriverUsername(incorrectUsername);
            fail("Exception was't thrown");
        } catch (Exception e) {
            assertEquals(AssignmentNotFoundException.class, e.getClass());
            assertEquals(String.format("No assignment for such driver's username: %s", incorrectUsername),
                         e.getMessage());
        }
    }

    @Test
    void openAssignment_shouldCreateAssignmentWithRelatedEntitiesAndCallSaveMethodOfAssignmentRepository() {
        final long driverId = 2L;
        final long busId = 3L;
        when(userService.findByUsername(correctUsername)).thenReturn(UserFactory.userInstance());
        when(driverService.findById(driverId)).thenReturn(DriverFactory.driverInstance());
        when(busService.findById(busId)).thenReturn(BusFactory.busInstance());

        assignmentService.openAssignment(driverId, busId, correctUsername);

        verify(assignmentRepository).save(assignmentWithRelations);
    }

    @Test
    void cancelAssignment_shouldCallAppropriateMethodsOfAssignmentRepository() {
        assignmentService.cancelAssignment(correctId);

        verify(assignmentRepository).removeAssignmentFromDriverAndBus(correctId);
        verify(assignmentRepository).deleteById(correctId);
    }

    @Test
    void acceptAssignment_shouldCallAppropriateMethodsOfAssignmentRepository() {
        assignmentService.acceptAssignment(correctId);

        verify(assignmentRepository).bindDriverWithBusAndRemoveAssignmentFromThem(correctId);
        verify(assignmentRepository).deleteById(correctId);
    }

    @Test
    void findAll_shouldCallFindAllMethodOfAssignmentRepository() {
        final Pageable pageable = PageableFactory.pageableInstance();
        when(assignmentRepository.findAll(pageable)).thenReturn(AssignmentFactory.assignmentPageInstance());

        assignmentService.findAll(pageable);

        verify(assignmentRepository).findAll(pageable);
    }
}
