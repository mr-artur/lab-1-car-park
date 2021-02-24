package ua.kpi.fict.carpark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.kpi.fict.carpark.entity.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {

    Page<Bus> findByDriverIsNullAndAssignmentIsNull(Pageable pageable);

    Page<Bus> findByRouteIsNull(Pageable pageable);

    @Modifying
    @Query("UPDATE Bus b SET b.route.id = :routeId WHERE b.id = :busId")
    void assignBusToRoute(@Param("busId") long busId, @Param("routeId") long routeId);

    @Modifying
    @Query("UPDATE Bus b SET b.route = null WHERE b.id = :busId")
    void unassignBusFromRoute(long busId);
}
