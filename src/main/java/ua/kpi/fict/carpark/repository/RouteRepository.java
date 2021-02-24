package ua.kpi.fict.carpark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.kpi.fict.carpark.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

    Page<Route> findAllByOrderByNumber(Pageable pageable);
}
