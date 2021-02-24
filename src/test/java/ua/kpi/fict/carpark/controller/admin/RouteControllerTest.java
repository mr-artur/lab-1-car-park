package ua.kpi.fict.carpark.controller.admin;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.controller.TestWithMockedSecurityBeans;
import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.dto.response.RouteDto;
import ua.kpi.fict.carpark.service.RouteService;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.RouteFactory;

@WebMvcTest(controllers = RouteController.class)
class RouteControllerTest extends TestWithMockedSecurityBeans {

    @MockBean
    private RouteService routeService;

    @Autowired
    private MockMvc mockMvc;

    private final Page<RouteDto> page = RouteFactory.routeDtosPageInstance();
    private final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    private final CreateRouteDto createRouteDto = RouteFactory.createRouteDtoInstance();

    @Test
    @WithMockAdmin
    void getAllRoutes_shouldCallFindAllSortedMethodOfRouteService() throws Exception {
        when(routeService.findAllSorted(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/routes")
                            .param("page", "5")
                            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/routes/all"))
            .andExpect(model().attribute("page", page))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(routeService).findAllSorted(pageableCaptor.capture());
    }

    @Test
    @WithMockSuperAdmin
    void getAllRoutes_shouldWorkForSuperAdmin() throws Exception {
        when(routeService.findAllSorted(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/routes"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("isSuperAdmin", true));
    }

    @Test
    @WithMockDriver
    void getAllRoutes_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(get("/admin/routes"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void addRoute_shouldReturnAddNewRoutePage() throws Exception {
        mockMvc.perform(get("/admin/routes/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/routes/new"))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockSuperAdmin
    void addRoute_shouldWorkForSuperAdmin() throws Exception {
        mockMvc.perform(get("/admin/routes/new"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("isSuperAdmin", true));
    }

    @Test
    @WithMockDriver
    void addRoute_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(get("/admin/routes/new"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void saveRoute_shouldCallSaveMethodOfRouteService_whenDtoIsValid() throws Exception {
        mockMvc.perform(post("/admin/routes/new")
                            .flashAttr("route", createRouteDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/routes/new?saved"));

        verify(routeService).save(createRouteDto);
    }

    @Test
    @WithMockAdmin
    void saveRoute_shouldNotCallSaveMethodOfRouteServiceAndReturnSamePageWithError_whenNumberIsNull() throws Exception {
        final CreateRouteDto dtoWithoutNumber = RouteFactory.createRouteDtoWithoutNumberInstance();

        mockMvc.perform(post("/admin/routes/new")
                            .flashAttr("bus", dtoWithoutNumber))
            .andExpect(view().name("admin/routes/new"))
            .andExpect(model().attributeHasFieldErrors("route", "number"))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(routeService, never()).save(dtoWithoutNumber);
    }

    @Test
    @WithMockAdmin
    void saveRoute_shouldNotCallSaveMethodOfRouteServiceAndReturnPageWithError_whenNumberIsTooLow() throws Exception {
        final CreateRouteDto dtoWithTooLowNumber = RouteFactory.createRouteDtoWithTooLowNumberInstance();

        mockMvc.perform(post("/admin/routes/new")
                            .flashAttr("bus", dtoWithTooLowNumber))
            .andExpect(view().name("admin/routes/new"))
            .andExpect(model().attributeHasFieldErrors("route", "number"))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(routeService, never()).save(dtoWithTooLowNumber);
    }

    @Test
    @WithMockAdmin
    void saveRoute_shouldNotCallSaveMethodOfRouteServiceAndReturnPageWithError_whenNumberIsTooBig() throws Exception {
        final CreateRouteDto dtoWithTooBigNumber = RouteFactory.createRouteDtoWithTooBigNumberInstance();

        mockMvc.perform(post("/admin/routes/new")
                            .flashAttr("bus", dtoWithTooBigNumber))
            .andExpect(view().name("admin/routes/new"))
            .andExpect(model().attributeHasFieldErrors("route", "number"))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(routeService, never()).save(dtoWithTooBigNumber);
    }

    @Test
    @WithMockSuperAdmin
    void saveRoute_shouldWorkForSuperAdmin() throws Exception {
        mockMvc.perform(post("/admin/routes/new")
                            .flashAttr("route", createRouteDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/routes/new?saved"));
    }

    @Test
    @WithMockDriver
    void saveRoute_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(post("/admin/routes/new"))
            .andExpect(status().isForbidden());
    }
}
