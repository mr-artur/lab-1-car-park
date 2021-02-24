package ua.kpi.fict.carpark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ua.kpi.fict.carpark.entity.Driver;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUserUsername(String username);

    Page<Driver> findByBusIsNullAndAssignmentIsNull(Pageable pageable);

    @Modifying
    @Query(value = "UPDATE drivers, buses "
        + "SET drivers.bus_id = null,"
        + "buses.driver_id = null "
        + "WHERE drivers.id = :driverId "
        + "AND buses.driver_id = :driverId", nativeQuery = true)
    void unassignDriverFromBus(long driverId);
}
