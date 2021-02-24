package ua.kpi.fict.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.kpi.fict.carpark.dto.response.AssignmentDto;
import ua.kpi.fict.carpark.entity.Assignment;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.exception.AssignmentNotFoundException;
import ua.kpi.fict.carpark.mapper.assignment.LocalizedAssignmentMapper;
import ua.kpi.fict.carpark.repository.AssignmentRepository;
import ua.kpi.fict.carpark.service.AssignmentService;
import ua.kpi.fict.carpark.service.BusService;
import ua.kpi.fict.carpark.service.DriverService;
import ua.kpi.fict.carpark.service.UserService;

@RequiredArgsConstructor
@Log4j2
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final BusService busService;
    private final DriverService driverService;
    private final UserService userService;
    private final LocalizedAssignmentMapper mapper;

    @Override
    public AssignmentDto findByDriverUsername(String username) {
        return assignmentRepository.findByDriverUserUsername(username).map(mapper::toDto)
            .orElseThrow(() -> new AssignmentNotFoundException(
                String.format("No assignment for such driver's username: %s", username)));
    }

    @Override
    public void openAssignment(long driverId, long busId, String initiatorUsername) {
        log.debug("Request to open assignment for driver with id : {}, bus with id : {}, initiator with username : {}",
                  driverId, busId, initiatorUsername);
        User initiator = userService.findByUsername(initiatorUsername);
        Driver driver = driverService.findById(driverId);
        Bus bus = busService.findById(busId);
        assignmentRepository.save(createAssignment(driver, bus, initiator));
        log.debug("Assignment for driver : {}, bus : {}, initiator : {} was saved", driver, bus, initiator);
    }

    private Assignment createAssignment(Driver driver, Bus bus, User initiator) {
        Assignment assignment = Assignment.builder().driver(driver).bus(bus).initiator(initiator).build();
        createReferencesInDriverAndBus(assignment);
        return assignment;
    }

    private void createReferencesInDriverAndBus(Assignment assignment) {
        assignment.getDriver().setAssignment(assignment);
        assignment.getBus().setAssignment(assignment);
    }

    @Override
    @Transactional
    public void cancelAssignment(long assignmentId) {
        log.debug("Request to cancel assignment with id : {}", assignmentId);
        assignmentRepository.removeAssignmentFromDriverAndBus(assignmentId);
        assignmentRepository.deleteById(assignmentId);
        log.debug("Assignment with id : {} was canceled", assignmentId);
    }

    @Override
    @Transactional
    public void acceptAssignment(long assignmentId) {
        log.debug("Request to accept assignment with id : {}", assignmentId);
        assignmentRepository.bindDriverWithBusAndRemoveAssignmentFromThem(assignmentId);
        assignmentRepository.deleteById(assignmentId);
        log.debug("Assignment with id : {} was accepted", assignmentId);
    }

    @Override
    public Page<AssignmentDto> findAll(Pageable pageable) {
        return assignmentRepository.findAll(pageable).map(mapper::toDto);
    }
}
