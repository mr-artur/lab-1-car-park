package ua.kpi.fict.carpark.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.kpi.fict.carpark.dto.response.AssignmentDto;
import ua.kpi.fict.carpark.entity.Assignment;

public interface AssignmentService {

    AssignmentDto findByDriverUsername(String username);

    void openAssignment(long driverId, long busId, String initiatorUsername);

    void cancelAssignment(long assignmentId);

    void acceptAssignment(long assignmentId);

    Page<AssignmentDto> findAll(Pageable pageable);
}
