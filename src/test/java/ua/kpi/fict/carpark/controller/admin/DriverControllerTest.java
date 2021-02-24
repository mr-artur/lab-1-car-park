package ua.kpi.fict.carpark.controller.admin;

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
import ua.kpi.fict.carpark.dto.response.DriverDto;
import ua.kpi.fict.carpark.service.DriverService;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.DriverFactory;

@WebMvcTest(controllers = DriverController.class)
class DriverControllerTest extends TestWithMockedSecurityBeans {

    @MockBean
    private DriverService driverService;

    @Autowired
    private MockMvc mockMvc;

    private final Page<DriverDto> page = DriverFactory.driverDtosPageInstance();
    private final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

    @Test
    @WithMockAdmin
    void getAllDrivers_shouldCallFindAllMethodOfDriverService() throws Exception {
        when(driverService.findAll(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/drivers")
                            .param("page", "5")
                            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/drivers/all"))
            .andExpect(model().attribute("page", page))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(driverService).findAll(pageableCaptor.capture());
    }

    @Test
    @WithMockSuperAdmin
    void getAllDrivers_shouldWorkForSuperAdmin() throws Exception {
        when(driverService.findAll(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/drivers"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("isSuperAdmin", true));
    }

    @Test
    @WithMockDriver
    void getAllDrivers_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(get("/admin/drivers"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void getDriversWithoutBusAndOpenedAssignment_shouldCallAppropriateMethodOfDriverService() throws Exception {
        final long busId = 1L;
        when(driverService.findAllWithoutBusAndOpenedAssignment(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/drivers/assign")
                            .param("page", "5")
                            .param("size", "10")
                            .param("busId", String.valueOf(busId)))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/drivers/assignToBus"))
            .andExpect(model().attribute("page", page))
            .andExpect(model().attribute("busId", busId))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(driverService).findAllWithoutBusAndOpenedAssignment(pageableCaptor.capture());
    }

    @Test
    @WithMockSuperAdmin
    void getDriversWithoutBusAndOpenedAssignment_shouldWorkForSuperAdmin() throws Exception {
        final long busId = 1L;
        when(driverService.findAllWithoutBusAndOpenedAssignment(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/drivers/assign")
                            .param("busId", String.valueOf(busId)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("isSuperAdmin", true));
    }

    @Test
    @WithMockDriver
    void getDriversWithoutBusAndOpenedAssignment_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(get("/admin/drivers/assign"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void unassignDriverFromBus_shouldCallAppropriateMethodOfDriverService() throws Exception {
        final long driverId = 1L;

        mockMvc.perform(post("/admin/drivers/" + driverId + "/unassign")
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?unassignedDriverFromBus"));

        verify(driverService).unassignDriverFromBus(driverId);
    }

    @Test
    @WithMockSuperAdmin
    void unassignDriverFromBus_shouldWorkForSuperAdmin() throws Exception {
        final long driverId = 1L;

        mockMvc.perform(post("/admin/drivers/" + driverId + "/unassign")
                            .param("redirect", "drivers"))
            .andExpect(status().isFound());
    }

    @Test
    @WithMockDriver
    void unassignDriverFromBus_shouldNotWorkForDriver() throws Exception {
        final long driverId = 1L;

        mockMvc.perform(post("/admin/drivers/" + driverId + "/unassign"))
            .andExpect(status().isForbidden());
    }
}
