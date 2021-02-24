package ua.kpi.fict.carpark.integration.admin;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.jupiter.api.Assertions.assertNull;

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

import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.integration.TestWithMockedDataWriter;
import ua.kpi.fict.carpark.repository.BusRepository;
import ua.kpi.fict.carpark.repository.DriverRepository;
import ua.kpi.fict.carpark.repository.UserRepository;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.factories.BusFactory;
import ua.kpi.fict.carpark.util.factories.DriverFactory;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
class DriverControllerIntegrationTest extends TestWithMockedDataWriter {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusRepository busRepository;

    private final User user = UserFactory.userInstance();
    private final Driver driver = DriverFactory.driverInstance(user);
    private final Bus bus = BusFactory.busInstance();

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        busRepository.save(bus);
        driverRepository.save(driver);
    }

    @AfterEach
    void tearDown() {
        driverRepository.deleteAll();
        userRepository.deleteAll();
        busRepository.deleteAll();
    }

    @Test
    @WithMockAdmin
    void getAllDrivers_shouldReturnPageWithAllBuses() throws Exception {
        mockMvc.perform(get("/admin/drivers"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/drivers/all"))
            .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
            .andExpect(model().attribute("page", hasProperty("content", hasItem(
                allOf(
                    hasProperty("id", is(driver.getId())),
                    hasProperty("user", allOf(
                        hasProperty("id", is(user.getId())),
                        hasProperty("username", is(user.getUsername())),
                        hasProperty("role", is(user.getRole())),
                        hasProperty("firstName", is(user.getFirstName())),
                        hasProperty("lastName", is(user.getLastName()))
                    )),
                    hasProperty("assignment", is(nullValue())),
                    hasProperty("bus", is(nullValue()))
                )
            ))))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockAdmin
    void getDriversWithoutBusAndOpenedAssignment_shouldReturnPageWithAllAppropriateDrivers() throws Exception {
        mockMvc.perform(get("/admin/drivers/assign")
                            .param("busId", String.valueOf(bus.getId())))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/drivers/assignToBus"))
            .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
            .andExpect(model().attribute("page", hasProperty("content", hasItem(
                allOf(
                    hasProperty("id", is(driver.getId())),
                    hasProperty("user", allOf(
                        hasProperty("id", is(user.getId())),
                        hasProperty("username", is(user.getUsername())),
                        hasProperty("role", is(user.getRole())),
                        hasProperty("firstName", is(user.getFirstName())),
                        hasProperty("lastName", is(user.getLastName()))
                    )),
                    hasProperty("assignment", is(nullValue())),
                    hasProperty("bus", is(nullValue()))
                )
            ))))
            .andExpect(model().attribute("busId", bus.getId()))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockAdmin
    void unassignDriverFromBus_shouldDestroyRelationsBetweenDriverAndBus() throws Exception {
        assignDriverToBusAndSave();

        mockMvc.perform(post("/admin/drivers/" + driver.getId() + "/unassign")
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?unassignedDriverFromBus"));

        assertNull(driverRepository.findById(driver.getId()).get().getBus());
        assertNull(busRepository.findById(bus.getId()).get().getDriver());
    }

    private void assignDriverToBusAndSave() {
        driver.setBus(bus);
        bus.setDriver(driver);
        driverRepository.save(driver);
        busRepository.save(bus);
    }
}
