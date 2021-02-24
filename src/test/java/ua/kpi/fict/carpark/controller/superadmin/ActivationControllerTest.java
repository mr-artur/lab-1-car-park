package ua.kpi.fict.carpark.controller.superadmin;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.controller.TestWithMockedSecurityBeans;
import ua.kpi.fict.carpark.dto.response.UserDto;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@WebMvcTest(controllers = ActivationController.class)
class ActivationControllerTest extends TestWithMockedSecurityBeans {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockSuperAdmin
    void getNotActivatedUsers_shouldCallFindNotActivatedMethodOfUserService() throws Exception {
        final Page<UserDto> page = UserFactory.userDtosPageInstance();
        final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        when(getUserService().findNotActivated(pageableCaptor.capture())).thenReturn(page);

        mockMvc.perform(get("/superadmin/activate")
                            .param("page", "5")
                            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("superadmin/activation/activate"))
            .andExpect(model().attribute("page", page));

        verify(getUserService()).findNotActivated(pageableCaptor.capture());
    }

    @Test
    @WithMockAdmin
    void getNotActivatedUsers_shouldNotWorkForAdmin() throws Exception {
        mockMvc.perform(get("/superadmin/activate"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockDriver
    void getNotActivatedUsers_shouldNotWorkForDriver() throws Exception {
        mockMvc.perform(get("/superadmin/activate"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockSuperAdmin
    void approveActivation_shouldCallActivateMethodOfUserService() throws Exception {
        final long userId = 1L;

        mockMvc.perform(post("/superadmin/activate/" + userId))
            .andExpect(view().name("redirect:/superadmin/activate?activated"));

        verify(getUserService()).activate(userId);
    }

    @Test
    @WithMockAdmin
    void approveActivation_shouldNotWorkForAdmin() throws Exception {
        final long userId = 1L;

        mockMvc.perform(post("/superadmin/activate/" + userId))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockDriver
    void approveActivation_shouldNotWorkForDriver() throws Exception {
        final long userId = 1L;

        mockMvc.perform(post("/superadmin/activate/" + userId))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockSuperAdmin
    void rejectActivation_shouldCallDeleteByIdMethodOfUserService() throws Exception {
        final long userId = 1L;

        mockMvc.perform(post("/superadmin/activate/reject/" + userId))
            .andExpect(view().name("redirect:/superadmin/activate?deleted"));

        verify(getUserService()).deleteById(userId);
    }

    @Test
    @WithMockAdmin
    void rejectActivation_shouldNotWorkForAdmin() throws Exception {
        final long userId = 1L;

        mockMvc.perform(post("/superadmin/activate/reject/" + userId))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockDriver
    void rejectActivation_shouldNotWorkForDriver() throws Exception {
        final long userId = 1L;

        mockMvc.perform(post("/superadmin/activate/reject/" + userId))
            .andExpect(status().isForbidden());
    }
}
