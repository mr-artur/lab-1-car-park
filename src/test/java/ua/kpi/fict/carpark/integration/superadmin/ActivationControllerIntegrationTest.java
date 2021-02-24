package ua.kpi.fict.carpark.integration.superadmin;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.integration.TestWithMockedDataWriter;
import ua.kpi.fict.carpark.repository.DriverRepository;
import ua.kpi.fict.carpark.repository.UserRepository;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
class ActivationControllerIntegrationTest extends TestWithMockedDataWriter {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    private final User user = UserFactory.userInstance();

    @BeforeEach
    void setUp() {
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        driverRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockSuperAdmin
    void getNotActivatedUsers_shouldReturnPageWithAllNotActivatedUsers() throws Exception {
        mockMvc.perform(get("/superadmin/activate"))
            .andExpect(status().isOk())
            .andExpect(view().name("superadmin/activation/activate"))
            .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
            .andExpect(model().attribute("page", hasProperty("content", hasItem(
                allOf(
                    hasProperty("id", is(user.getId())),
                    hasProperty("username", is(user.getUsername())),
                    hasProperty("role", is(user.getRole())),
                    hasProperty("firstName", is(user.getFirstName())),
                    hasProperty("lastName", is(user.getLastName()))
                )
            ))));
    }

    @Test
    @WithMockSuperAdmin
    void approveActivation_shouldActivateUserAndSaveAsDriverAndRedirectToAppropriatePage() throws Exception {
        mockMvc.perform(post("/superadmin/activate/" + user.getId()))
            .andExpect(view().name("redirect:/superadmin/activate?activated"));

        User found = userRepository.findById(user.getId()).get();

        assertTrue(found.isAccountNonLocked());
        assertTrue(found.isAccountNonExpired());
        assertTrue(found.isCredentialsNonExpired());
        assertTrue(found.isEnabled());
        assertEquals(1, driverRepository.count());
    }

    @Test
    @WithMockSuperAdmin
    void rejectActivation_shouldDeleteUser() throws Exception {
        mockMvc.perform(post("/superadmin/activate/reject/" + user.getId()))
            .andExpect(view().name("redirect:/superadmin/activate?deleted"));

        assertEquals(0, userRepository.count());
    }
}
