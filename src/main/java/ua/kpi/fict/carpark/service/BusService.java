package ua.kpi.fict.carpark.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.dto.response.BusDto;
import ua.kpi.fict.carpark.entity.Bus;

public interface BusService {

    void save(CreateBusDto createBusDto);

    Bus findById(long busId);

    Page<BusDto> findAll(Pageable pageable);

    Page<BusDto> findAllWithoutRoute(Pageable pageable);

    Page<BusDto> findAllWithoutDriverAndOpenedAssignment(Pageable pageable);

    void assignBusToRoute(long busId, long routeId);

    void unassignBusFromRoute(long busId);
}
