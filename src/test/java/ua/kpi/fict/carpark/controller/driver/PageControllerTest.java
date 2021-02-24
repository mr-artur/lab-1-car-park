package ua.kpi.fict.carpark.controller.driver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.controller.TestWithMockedSecurityBeans;
import ua.kpi.fict.carpark.dto.response.DriverDto;
import ua.kpi.fict.carpark.dto.response.UserDto;
import ua.kpi.fict.carpark.service.DriverService;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.DriverFactory;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@WebMvcTest(controllers = PageController.class)
class PageControllerTest extends TestWithMockedSecurityBeans {

    @MockBean
    private DriverService driverService;

    @Autowired
    private MockMvc mockMvc;

    private final String username = "karimlegoida";

    @Test
    @WithMockDriver
    void getHomePage_shouldCallAppropriateMethodOfUserServiceAndReturnHomePage() throws Exception {
        final UserDto userDto = UserFactory.userDtoInstance();
        when(getUserService().findLocalizedByUsername(username)).thenReturn(userDto);

        mockMvc.perform(get("/driver/home"))
            .andExpect(status().isOk())
            .andExpect(view().name("driver/home"))
            .andExpect(model().attribute("user", userDto));

        verify(getUserService()).findLocalizedByUsername(username);
    }

    @Test
    @WithMockSuperAdmin
    void getHomePage_shouldNotWorkForSuperAdmin() throws Exception {
        mockMvc.perform(get("/driver/home"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void getHomePage_shouldNotWorkForAdmin() throws Exception {
        mockMvc.perform(get("/driver/home"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockDriver
    void getJobPage_shouldCallFindByUsernameMethodOfDriverServiceAndReturnJobPage() throws Exception {
        final DriverDto driverDto = DriverFactory.driverDtoInstance();
        when(driverService.findByUsername(username)).thenReturn(driverDto);

        mockMvc.perform(get("/driver/job"))
            .andExpect(status().isOk())
            .andExpect(view().name("driver/job"))
            .andExpect(model().attribute("driver", driverDto));

        verify(driverService).findByUsername(username);
    }

    @Test
    @WithMockSuperAdmin
    void getJobPage_shouldNotWorkForSuperAdmin() throws Exception {
        mockMvc.perform(get("/driver/job"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void getJobPage_shouldNotWorkForAdmin() throws Exception {
        mockMvc.perform(get("/driver/job"))
            .andExpect(status().isForbidden());
    }
}
