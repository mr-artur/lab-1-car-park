package ua.kpi.fict.carpark.controller.driver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.controller.TestWithMockedSecurityBeans;
import ua.kpi.fict.carpark.dto.response.AssignmentDto;
import ua.kpi.fict.carpark.exception.AssignmentNotFoundException;
import ua.kpi.fict.carpark.service.AssignmentService;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.AssignmentFactory;

@WebMvcTest(controllers = AssignmentAcceptanceController.class)
class AssignmentAcceptanceControllerTest extends TestWithMockedSecurityBeans {

    @MockBean
    private AssignmentService assignmentService;

    @Autowired
    private MockMvc mockMvc;

    private final String username = "karimlegoida";

    @Test
    @WithMockDriver
    void getAssignment_shouldCallAppropriateMethodOfAssignmentServiceAndReturnAssignmentPage() throws Exception {
        final AssignmentDto assignmentDto = AssignmentFactory.assignmentDtoInstance();
        when(assignmentService.findByDriverUsername(username)).thenReturn(assignmentDto);

        mockMvc.perform(get("/driver/assignments"))
            .andExpect(status().isOk())
            .andExpect(view().name("driver/assignment"))
            .andExpect(model().attribute("assignment", assignmentDto));

        verify(assignmentService).findByDriverUsername(username);
    }

    @Test
    @WithMockSuperAdmin
    void getAssignment_shouldNotWorkForSuperAdmin() throws Exception {
        mockMvc.perform(get("/driver/assignment"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void getAssignment_shouldNotWorkForAdmin() throws Exception {
        mockMvc.perform(get("/driver/assignment"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockDriver
    void handleAssignmentAbsence_shouldHandleAppropriateExceptionAndReturnPageWithCorrectAttributes() throws Exception {
        when(assignmentService.findByDriverUsername(username)).thenThrow(AssignmentNotFoundException.class);

        mockMvc.perform(get("/driver/assignments"))
            .andExpect(status().isOk())
            .andExpect(view().name("driver/assignment"))
            .andExpect(model().attribute("notFound", true));
    }

    @Test
    @WithMockDriver
    void acceptAssignment_shouldCallAppropriateMethodOfAssignmentServiceAndReturnRedirect() throws Exception {
        final long assignmentId = 1L;

        mockMvc.perform(post("/driver/assignments/accept/" + assignmentId))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/driver/job?assignmentAccepted"));

        verify(assignmentService).acceptAssignment(assignmentId);
    }

    @Test
    @WithMockSuperAdmin
    void acceptAssignment_shouldNotWorkForSuperAdmin() throws Exception {
        final long assignmentId = 1L;

        mockMvc.perform(post("/driver/assignments/accept/" + assignmentId))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockAdmin
    void acceptAssignment_shouldNotWorkForAdmin() throws Exception {
        final long assignmentId = 1L;

        mockMvc.perform(post("/driver/assignments/accept/" + assignmentId))
            .andExpect(status().isForbidden());
    }
}
