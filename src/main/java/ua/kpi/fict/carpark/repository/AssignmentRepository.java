package ua.kpi.fict.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ua.kpi.fict.carpark.entity.Assignment;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    Optional<Assignment> findByDriverUserUsername(String username);

    @Modifying
    @Query(value = "UPDATE drivers, buses "
        + "SET drivers.assignment_id = null, "
        + "buses.assignment_id = null "
        + "WHERE drivers.assignment_id = :assignmentId "
        + "AND buses.assignment_id = :assignmentId", nativeQuery = true)
    void removeAssignmentFromDriverAndBus(long assignmentId);

    @Modifying
    @Query(value = "UPDATE drivers, buses "
        + "SET drivers.bus_id = (SELECT id FROM (SELECT b.id FROM buses b WHERE b.assignment_id = :assignmentId) AS id), "
        + "buses.assignment_id = null, "
        + "buses.driver_id = (SELECT id FROM (SELECT d.id FROM drivers d WHERE d.assignment_id = :assignmentId) AS id), "
        + "drivers.assignment_id = null "
        + "WHERE drivers.assignment_id = :assignmentId "
        + "AND buses.assignment_id = :assignmentId", nativeQuery = true)
    void bindDriverWithBusAndRemoveAssignmentFromThem(long assignmentId);
}
