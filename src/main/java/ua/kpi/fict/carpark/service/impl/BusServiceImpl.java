package ua.kpi.fict.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.dto.response.BusDto;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.exception.BusNotFoundException;
import ua.kpi.fict.carpark.mapper.bus.LocalizedBusMapper;
import ua.kpi.fict.carpark.repository.BusRepository;
import ua.kpi.fict.carpark.service.BusService;

@RequiredArgsConstructor
@Log4j2
@Service
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final LocalizedBusMapper mapper;

    @Override
    public void save(CreateBusDto createBusDto) {
        log.debug("Request to save bus : {}", createBusDto);
        Bus bus = mapper.toBus(createBusDto);
        busRepository.save(bus);
        log.debug("Saved bus : {}", bus);
    }

    @Override
    public Bus findById(long busId) {
        return busRepository.findById(busId)
            .orElseThrow(() -> new BusNotFoundException(String.format("No bus found with such id : %s", busId)));
    }

    @Override
    public Page<BusDto> findAll(Pageable pageable) {
        return busRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<BusDto> findAllWithoutRoute(Pageable pageable) {
        return busRepository.findByRouteIsNull(pageable).map(mapper::toDto);
    }

    @Override
    public Page<BusDto> findAllWithoutDriverAndOpenedAssignment(Pageable pageable) {
        return busRepository.findByDriverIsNullAndAssignmentIsNull(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void assignBusToRoute(long busId, long routeId) {
        log.debug("Request to assign bus with id : {} to route with id : {}", busId, routeId);
        busRepository.assignBusToRoute(busId, routeId);
        log.debug("Assigned bus with id : {} to route with id : {}", busId, routeId);
    }

    @Override
    @Transactional
    public void unassignBusFromRoute(long busId) {
        log.debug("Request to unassign bus with id : {} from route", busId);
        busRepository.unassignBusFromRoute(busId);
        log.debug("Unassigned bus with id : {} from route", busId);
    }
}
