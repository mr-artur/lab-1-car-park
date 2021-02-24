package ua.kpi.fict.carpark.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Pageable;

import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.entity.Route;
import ua.kpi.fict.carpark.util.factories.PageableFactory;
import ua.kpi.fict.carpark.util.factories.RouteFactory;
import ua.kpi.fict.carpark.mapper.route.LocalizedRouteMapper;
import ua.kpi.fict.carpark.repository.RouteRepository;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private LocalizedRouteMapper mapper;

    @InjectMocks
    private RouteServiceImpl routeService;

    @Test
    void save_shouldCallSaveMethodOfRouteRepository() {
        final CreateRouteDto dto = RouteFactory.createRouteDtoInstance();
        final Route route = RouteFactory.routeInstance();
        when(mapper.toRoute(dto)).thenReturn(route);

        routeService.save(dto);

        verify(routeRepository).save(route);
    }

    @Test
    void findAllSorted_shouldCallFindAllByOrderByNumberMethodOfRouteRepository() {
        final Pageable pageable = PageableFactory.pageableInstance();
        when(routeRepository.findAllByOrderByNumber(pageable)).thenReturn(RouteFactory.routePageInstance());

        routeService.findAllSorted(pageable);

        verify(routeRepository).findAllByOrderByNumber(pageable);
    }
}
