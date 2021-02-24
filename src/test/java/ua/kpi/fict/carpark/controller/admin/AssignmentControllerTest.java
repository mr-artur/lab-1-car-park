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
import ua.kpi.fict.carpark.dto.response.AssignmentDto;
import ua.kpi.fict.carpark.service.AssignmentService;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.AssignmentFactory;

@WebMvcTest(controllers = AssignmentController.class)
class AssignmentControllerTest extends TestWithMockedSecurityBeans {

    @MockBean
    private AssignmentService assignmentService;

    @Autowired
    private MockMvc mockMvc;

    private final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    private final Page<AssignmentDto> page = AssignmentFactory.assignmentDtosPageInstance();

    @Test
    @WithMockAdmin
    void getAllAssignments_shouldCallFindAllMethodOfAssignmentService() throws Exception {
        when(assignmentService.findAll(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/assignments")
                            .param("page", "5")
                            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/assignments/all"))
            .andExpect(model().attribute("page", page))
            .andExpect(model().attribute("isSuperAdmin", false));

        verify(assignmentService).findAll(pageableCaptor.capture());
    }

    @Test
    @WithMockSuperAdmin
    void getAllAssignments_shouldWorkForSuperAdmin() throws Exception {
        when(assignmentService.findAll(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/admin/assignments"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("isSuperAdmin", true));
    }

    @Test
    @WithMockDriver
    void getAllAssignments_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(get("/admin/assignments"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void cancelAssignment_shouldCallCancelAssignmentMethodOfAssignmentService() throws Exception {
        final long assignmentId = 1L;

        mockMvc.perform(post("/admin/assignments/delete/" + assignmentId))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/assignments?assignmentCanceled"));

        verify(assignmentService).cancelAssignment(assignmentId);
    }

    @Test
    @WithMockSuperAdmin
    void cancelAssignment_shouldWorkForSuperAdmin() throws Exception {
        final long assignmentId = 1L;

        mockMvc.perform(post("/admin/assignments/delete/" + assignmentId))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/assignments?assignmentCanceled"));
    }

    @Test
    @WithMockDriver
    void cancelAssignment_shouldNotWorkForDriver() throws Exception {
        final long assignmentId = 1L;

        mockMvc.perform(post("/admin/assignments/delete/" + assignmentId))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void openAssignment_shouldCallOpenAssignmentMethodOfAssignmentService() throws Exception {
        final long driverId = 1L;
        final long busId = 2L;

        mockMvc.perform(post("/admin/assignments/driver/" + driverId + "/bus/" + busId)
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?driverToBusAssignmentOpened"));

        verify(assignmentService).openAssignment(driverId, busId, "genamytrofanov");
    }

    @Test
    @WithMockSuperAdmin
    void openAssignment_shouldWorkForSuperAdmin() throws Exception {
        final long driverId = 1L;
        final long busId = 2L;

        mockMvc
            .perform(post("/admin/assignments/driver/" + driverId + "/bus/" + busId)
                         .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?driverToBusAssignmentOpened"));
    }

    @Test
    @WithMockDriver
    void openAssignment_shouldNotWorkForDriver() throws Exception {
        final long driverId = 1L;
        final long busId = 2L;

        mockMvc
            .perform(post("/admin/assignments/driver/" + driverId + "/bus/" + busId)
                         .param("redirect", "drivers"))
            .andExpect(status().isForbidden());
    }
}
