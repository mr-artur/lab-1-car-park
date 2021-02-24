package ua.kpi.fict.carpark.integration.driver;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import ua.kpi.fict.carpark.entity.Assignment;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.integration.TestWithMockedDataWriter;
import ua.kpi.fict.carpark.repository.AssignmentRepository;
import ua.kpi.fict.carpark.repository.BusRepository;
import ua.kpi.fict.carpark.repository.DriverRepository;
import ua.kpi.fict.carpark.repository.UserRepository;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.factories.AssignmentFactory;
import ua.kpi.fict.carpark.util.factories.BusFactory;
import ua.kpi.fict.carpark.util.factories.DriverFactory;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
class AssignmentAcceptanceControllerIntegrationTest extends TestWithMockedDataWriter {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private BusRepository busRepository;

    private final User admin = UserFactory.activatedAdminInstance();
    private final User userDriver = UserFactory.activatedUserInstance();
    private final Driver driver = DriverFactory.driverInstance(userDriver);
    private final Bus bus = BusFactory.busInstance();
    private final Assignment assignment = AssignmentFactory.assignmentInstance(driver, bus, admin);

    @BeforeEach
    void setUp() {
        saveEntities();
        saveAssignmentToDriverAndBus();
    }

    private void saveEntities() {
        userRepository.save(userDriver);
        userRepository.save(admin);
        driverRepository.save(driver);
        busRepository.save(bus);
        assignmentRepository.save(assignment);
    }

    private void saveAssignmentToDriverAndBus() {
        saveAssignmentToDriver();
        saveAssignmentToBus();
    }

    private void saveAssignmentToBus() {
        bus.setAssignment(assignment);
        busRepository.save(bus);
    }

    private void saveAssignmentToDriver() {
        driver.setAssignment(assignment);
        driverRepository.save(driver);
    }

    @AfterEach
    void tearDown() {
        removeUnnecessaryReferences();
        clearRepositories();
    }

    private void removeUnnecessaryReferences() {
        removeReferencesFromBus();
        removeReferencesFromDriver();
    }

    private void removeReferencesFromBus() {
        bus.setAssignment(null);
        busRepository.save(bus);
    }

    private void removeReferencesFromDriver() {
        driver.setAssignment(null);
        driverRepository.save(driver);
    }

    private void clearRepositories() {
        assignmentRepository.deleteAll();
        busRepository.deleteAll();
        driverRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockDriver
    void getAssignment_shouldReturnPageWithAssignment_whenAssignmentIsPresent() throws Exception {
        mockMvc.perform(get("/driver/assignments"))
            .andExpect(status().isOk())
            .andExpect(view().name("driver/assignment"))
            .andExpect(model().attribute("assignment",
                                         allOf(
                                             hasProperty("id", is(assignment.getId())),
                                             hasProperty("driver",
                                                         hasProperty("user", allOf(
                                                             hasProperty("id", is(userDriver.getId())),
                                                             hasProperty("username", is(userDriver.getUsername())),
                                                             hasProperty("role", is(userDriver.getRole())),
                                                             hasProperty("firstName", is(userDriver.getFirstName())),
                                                             hasProperty("lastName", is(userDriver.getLastName()))
                                                         ))),
                                             hasProperty("bus", hasProperty("model", is(bus.getModel()))),
                                             hasProperty("initiator", hasProperty("username", is(admin.getUsername())))
                                         )
            ));
    }

    @Test
    @WithMockDriver
    void acceptAssignment_shouldCreateRelationsBetweenDriverAndBusAndDeleteAssignment() throws Exception {
        mockMvc.perform(post("/driver/assignments/accept/" + assignment.getId()))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/driver/job?assignmentAccepted"));

        assertEquals(bus, driverRepository.findById(driver.getId()).get().getBus());
        assertEquals(driver, busRepository.findById(bus.getId()).get().getDriver());
        assertEquals(0, assignmentRepository.count());
    }
}
