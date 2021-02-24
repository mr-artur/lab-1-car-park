package ua.kpi.fict.carpark.integration.admin;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import ua.kpi.fict.carpark.entity.Assignment;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;

import ua.kpi.fict.carpark.integration.TestWithMockedDataWriter;
import ua.kpi.fict.carpark.repository.AssignmentRepository;
import ua.kpi.fict.carpark.repository.BusRepository;
import ua.kpi.fict.carpark.repository.DriverRepository;
import ua.kpi.fict.carpark.repository.UserRepository;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.factories.AssignmentFactory;
import ua.kpi.fict.carpark.util.factories.BusFactory;
import ua.kpi.fict.carpark.util.factories.DriverFactory;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
class AssignmentControllerIntegrationTest extends TestWithMockedDataWriter {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    private final User admin = UserFactory.activatedAdminInstance();
    private final User userDriver = UserFactory.activatedUserInstance();
    private final Driver driver = DriverFactory.driverInstance(userDriver);
    private final Bus bus = BusFactory.busInstance();
    private final Assignment assignment = AssignmentFactory.assignmentInstance(driver, bus, admin);

    @BeforeEach
    void setUp() {
        userRepository.save(userDriver);
        userRepository.save(admin);
        userRepository.save(UserFactory.activatedSuperAdminInstance());
        driverRepository.save(driver);
        busRepository.save(bus);
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
    @WithMockAdmin
    void getAllAssignments_shouldReturnPageWithAllAssignments() throws Exception {
        assignmentRepository.save(assignment);

        mockMvc.perform(get("/admin/assignments"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/assignments/all"))
            .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
            .andExpect(model().attribute("page", hasProperty("content", hasItem(
                allOf(
                    hasProperty("driver",
                                hasProperty("user",
                                            hasProperty("username", is(driver.getUser().getUsername())))),
                    hasProperty("bus", hasProperty("model", is(bus.getModel()))),
                    hasProperty("initiator", hasProperty("username", is(admin.getUsername())))
                )
            ))))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockAdmin
    void cancelAssignment_shouldDeleteAssignmentAndRedirectToAssignmentsPage_whenIdIsCorrect() throws Exception {
        assignmentRepository.save(assignment);

        mockMvc.perform(post("/admin/assignments/delete/" + assignment.getId()))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/assignments?assignmentCanceled"));

        assertEquals(0, assignmentRepository.count());
        assertNull(busRepository.findById(bus.getId()).get().getAssignment());
        assertNull(driverRepository.findById(driver.getId()).get().getAssignment());
    }

    @Test
    @WithMockAdmin
    void handleAssignmentNonExistence_shouldRedirectToAllAssignmentsPageWithAppropriateMessage() throws Exception {
        assignmentRepository.save(assignment);
        assignmentRepository.delete(assignment);

        mockMvc.perform(post("/admin/assignments/delete/" + assignment.getId()))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/assignments?doesNotExist"));
    }

    @Test
    @WithMockAdmin
    void openAssignment_shouldCreateAssignmentAndRedirectToRequestedPage_whenIdsAreCorrect() throws Exception {
        mockMvc.perform(post("/admin/assignments/driver/" + driver.getId() + "/bus/" + bus.getId())
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?driverToBusAssignmentOpened"));

        assertEquals(1, assignmentRepository.count());
        assertNotNull(driverRepository.findById(driver.getId()).get().getAssignment());
        assertNotNull(busRepository.findById(bus.getId()).get().getAssignment());
    }

    @Test
    @WithMockAdmin
    void handleAssignmentWithSameParticipantsExistence_shouldRedirectWithAppropriateMessage() throws Exception {
        assignmentRepository.save(assignment);

        mockMvc.perform(post("/admin/assignments/driver/" + driver.getId() + "/bus/" + bus.getId())
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/assignments?alreadyExists"));

        assertEquals(1, assignmentRepository.count());
    }
}
