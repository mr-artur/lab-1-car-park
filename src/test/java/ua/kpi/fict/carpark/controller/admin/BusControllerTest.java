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
import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.dto.response.BusDto;
import ua.kpi.fict.carpark.service.BusService;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.BusFactory;

@WebMvcTest(controllers = BusController.class)
class BusControllerTest extends TestWithMockedSecurityBeans {

    @MockBean
    private BusService busService;

    @Autowired
    private MockMvc mockMvc;

    private final Page<BusDto> page = BusFactory.busDtosPageInstance();
    private final CreateBusDto createBusDto = BusFactory.createBusDtoInstance();
    private final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

    @Test
    @WithMockAdmin
    void getAllBuses_shouldCallFindAllMethodOfBusService() throws Exception {
        when(busService.findAll(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/buses")
                            .param("page", "5")
                            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/buses/all"))
            .andExpect(model().attribute("page", page))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(busService).findAll(pageableCaptor.capture());
    }

    @Test
    @WithMockSuperAdmin
    void getAllBuses_shouldWorkForSuperAdmin() throws Exception {
        when(busService.findAll(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/buses"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("isSuperAdmin", true));
    }

    @Test
    @WithMockDriver
    void getAllBuses_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(get("/admin/buses"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void addBus_shouldReturnAddNewBusPage() throws Exception {
        mockMvc.perform(get("/admin/buses/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/buses/new"))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockAdmin
    void saveBus_shouldCallSaveMethodOfBusService_whenDtoIsValid() throws Exception {
        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", createBusDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/buses/new?saved"));

        verify(busService).save(createBusDto);
    }

    @Test
    @WithMockAdmin
    void saveBus_shouldNotCallServiceMethodAndReturnSamePageWithErrorAttributes_whenModelIsNull() throws Exception {
        final CreateBusDto dtoWithoutModel = BusFactory.createBusDtoWithoutModelInstance();

        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", dtoWithoutModel))
            .andExpect(view().name("admin/buses/new"))
            .andExpect(model().attributeHasFieldErrors("bus", "model"))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(busService, never()).save(dtoWithoutModel);
    }

    @Test
    @WithMockAdmin
    void saveBus_shouldNotCallServiceMethodAndReturnPageWithError_whenModelDoesNotMatchPattern() throws Exception {
        final CreateBusDto dtoWithIncorrectModel = BusFactory.createBusDtoWithIncorrectModelInstance();

        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", dtoWithIncorrectModel))
            .andExpect(view().name("admin/buses/new"))
            .andExpect(model().attributeHasFieldErrors("bus", "model"))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(busService, never()).save(dtoWithIncorrectModel);
    }

    @Test
    @WithMockAdmin
    void saveBus_shouldNotCallServiceMethodAndReturnSamePageWithErrorAttributes_whenCapacityIsNull() throws Exception {
        final CreateBusDto dtoWithoutCapacity = BusFactory.createBusDtoWithoutCapacityInstance();

        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", dtoWithoutCapacity))
            .andExpect(view().name("admin/buses/new"))
            .andExpect(model().attributeHasFieldErrors("bus", "capacity"))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(busService, never()).save(dtoWithoutCapacity);
    }

    @Test
    @WithMockAdmin
    void saveBus_shouldNotCallServiceMethodAndReturnPageWithError_whenCapacityIsTooLow() throws Exception {
        final CreateBusDto dtoWithTooLowCapacity = BusFactory.createBusDtoWithTooLowCapacityInstance();

        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", dtoWithTooLowCapacity))
            .andExpect(view().name("admin/buses/new"))
            .andExpect(model().attributeHasFieldErrors("bus", "capacity"))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(busService, never()).save(dtoWithTooLowCapacity);
    }

    @Test
    @WithMockAdmin
    void saveBus_shouldNotCallServiceMethodAndReturnPageWithError_whenCapacityIsTooBig() throws Exception {
        final CreateBusDto dtoWithTooBigCapacity = BusFactory.createBusDtoWithTooBigCapacityInstance();

        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", dtoWithTooBigCapacity))
            .andExpect(view().name("admin/buses/new"))
            .andExpect(model().attributeHasFieldErrors("bus", "capacity"))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(busService, never()).save(dtoWithTooBigCapacity);
    }

    @Test
    @WithMockSuperAdmin
    void saveBus_shouldWorkForSuperAdmin() throws Exception {
        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", createBusDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/buses/new?saved"));
    }

    @Test
    @WithMockDriver
    void saveBus_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", createBusDto))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void getBusesWithoutRoute_shouldCallFindAllWithoutRouteMethodOfBusService() throws Exception {
        final long routeId = 1L;
        when(busService.findAllWithoutRoute(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/buses/assign")
                            .param("page", "5")
                            .param("size", "10")
                            .param("routeId", String.valueOf(routeId)))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/buses/assignToRoute"))
            .andExpect(model().attribute("page", page))
            .andExpect(model().attribute("routeId", routeId))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(busService).findAllWithoutRoute(pageableCaptor.capture());
    }

    @Test
    @WithMockSuperAdmin
    void getBusesWithoutRoute_shouldWorkForSuperAdmin() throws Exception {
        final long routeId = 1L;
        when(busService.findAllWithoutRoute(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/buses/assign")
                            .param("routeId", String.valueOf(routeId)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("isSuperAdmin", true));
    }

    @Test
    @WithMockDriver
    void getBusesWithoutRoute_shouldNotWorkForDriver() throws Exception {
        final long routeId = 1L;

        mockMvc.perform(get("/admin/buses/assign")
                            .param("routeId", String.valueOf(routeId)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void getBusesWithoutDriver_shouldCallAppropriateMethodOfBusService() throws Exception {
        final long driverId = 1L;
        when(busService.findAllWithoutDriverAndOpenedAssignment(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/buses/assign")
                            .param("page", "5")
                            .param("size", "10")
                            .param("driverId", String.valueOf(driverId)))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/buses/assignToDriver"))
            .andExpect(model().attribute("page", page))
            .andExpect(model().attribute("driverId", driverId))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(busService).findAllWithoutDriverAndOpenedAssignment(pageableCaptor.capture());
    }

    @Test
    @WithMockSuperAdmin
    void getBusesWithoutDriver_shouldWorkForSuperAdmin() throws Exception {
        final long driverId = 1L;
        when(busService.findAllWithoutDriverAndOpenedAssignment(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/buses/assign")
                            .param("driverId", String.valueOf(driverId)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("isSuperAdmin", true));
    }

    @Test
    @WithMockDriver
    void getBusesWithoutDriver_shouldNotWorkForDriver() throws Exception {
        final long driverId = 1L;

        mockMvc.perform(get("/admin/buses/assign")
                            .param("driverId", String.valueOf(driverId)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void assignBusToRoute_shouldCallAssignBusToRouteMethodOfBusService() throws Exception {
        final long busId = 1L;
        final long routeId = 2L;

        mockMvc.perform(post("/admin/buses/" + busId + "/route/" + routeId)
                            .param("redirect", "buses"))
            .andExpect(view().name("redirect:/admin/buses?assignedBusToRoute"));

        verify(busService).assignBusToRoute(busId, routeId);
    }

    @Test
    @WithMockSuperAdmin
    void assignBusToRoute_shouldWorkForSuperAdmin() throws Exception {
        final long busId = 1L;
        final long routeId = 2L;

        mockMvc.perform(post("/admin/buses/" + busId + "/route/" + routeId)
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?assignedBusToRoute"));
    }

    @Test
    @WithMockDriver
    void assignBusToRoute_shouldNotWorkForDriver() throws Exception {
        final long busId = 1L;
        final long routeId = 2L;

        mockMvc.perform(post("/admin/buses/" + busId + "/route/" + routeId)
                            .param("redirect", "drivers"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void unassignBusFromRoute_shouldCallUnassignBusFromRouteMethodOfBusService() throws Exception {
        final long busId = 1L;

        mockMvc.perform(post("/admin/buses/" + busId + "/unassign")
                            .param("redirect", "buses"))
            .andExpect(view().name("redirect:/admin/buses?unassignedBusFromRoute"));

        verify(busService).unassignBusFromRoute(busId);
    }

    @Test
    @WithMockSuperAdmin
    void unassignBusFromRoute_shouldWorkForSuperAdmin() throws Exception {
        final long busId = 1L;

        mockMvc.perform(post("/admin/buses/" + busId + "/unassign")
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?unassignedBusFromRoute"));
    }

    @Test
    @WithMockDriver
    void unassignBusFromRoute_shouldNotWorkForDriver() throws Exception {
        final long busId = 1L;

        mockMvc.perform(post("/admin/buses/" + busId + "/unassign")
                            .param("redirect", "drivers"))
            .andExpect(status().isForbidden());
    }
}
