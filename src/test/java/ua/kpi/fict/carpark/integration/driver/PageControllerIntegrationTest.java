package ua.kpi.fict.carpark.integration.driver;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.integration.TestWithMockedDataWriter;
import ua.kpi.fict.carpark.repository.DriverRepository;
import ua.kpi.fict.carpark.repository.UserRepository;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.factories.DriverFactory;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
class PageControllerIntegrationTest extends TestWithMockedDataWriter {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    private final User user = UserFactory.activatedUserInstance();
    private final Driver driver = DriverFactory.driverInstance(user);

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        driverRepository.save(driver);
    }

    @AfterEach
    void tearDown() {
        driverRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockDriver
    void getHomePage_shouldReturnDriverHomePageWithAppropriateAttributes() throws Exception {
        mockMvc.perform(get("/driver/home"))
            .andExpect(status().isOk())
            .andExpect(view().name("driver/home"))
            .andExpect(model().attribute("user", allOf(
                hasProperty("id", is(user.getId())),
                hasProperty("username", is(user.getUsername())),
                hasProperty("role", is(user.getRole())),
                hasProperty("firstName", is(user.getFirstName())),
                hasProperty("lastName", is(user.getLastName()))
            )));
    }

    @Test
    @WithMockDriver
    void getJobPage_shouldReturnDriverJobPageWithAppropriateAttributes() throws Exception {
        mockMvc.perform(get("/driver/job"))
            .andExpect(status().isOk())
            .andExpect(view().name("driver/job"))
            .andExpect(model().attribute("driver",
                                         allOf(
                                             hasProperty("id", is(driver.getId())),
                                             hasProperty("bus", is(nullValue())),
                                             hasProperty("assignment", is(nullValue())),
                                             hasProperty("user",
                                                         allOf(
                                                             hasProperty("id", is(user.getId())),
                                                             hasProperty("username", is(user.getUsername())),
                                                             hasProperty("role", is(user.getRole())),
                                                             hasProperty("firstName", is(user.getFirstName())),
                                                             hasProperty("lastName", is(user.getLastName()))
                                                         ))
                                         )
            ));
    }
}
