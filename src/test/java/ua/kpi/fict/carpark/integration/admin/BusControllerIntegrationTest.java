package ua.kpi.fict.carpark.integration.admin;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Route;
import ua.kpi.fict.carpark.integration.TestWithMockedDataWriter;
import ua.kpi.fict.carpark.repository.BusRepository;
import ua.kpi.fict.carpark.repository.RouteRepository;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.factories.BusFactory;
import ua.kpi.fict.carpark.util.factories.RouteFactory;

@SpringBootTest
@AutoConfigureMockMvc
class BusControllerIntegrationTest extends TestWithMockedDataWriter {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private RouteRepository routeRepository;

    private final Bus bus = BusFactory.busInstance();
    private final CreateBusDto createBusDto = BusFactory.createBusDtoInstance();
    private final Route route = RouteFactory.routeInstance();

    @AfterEach
    void tearDown() {
        busRepository.deleteAll();
        routeRepository.deleteAll();
    }

    @Test
    @WithMockAdmin
    void getAllBuses_shouldReturnPageWithAllBuses() throws Exception {
        busRepository.save(bus);

        mockMvc.perform(get("/admin/buses"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/buses/all"))
            .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
            .andExpect(model().attribute("page", hasProperty("content", hasItem(
                allOf(
                    hasProperty("driver", is(nullValue())),
                    hasProperty("assignment", is(nullValue())),
                    hasProperty("model", is(bus.getModel())),
                    hasProperty("capacity", is(bus.getCapacity()))
                )
            ))))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockAdmin
    void saveBus_shouldSaveBus_whenCreateBusDtoIsValid() throws Exception {
        mockMvc.perform(post("/admin/buses/new")
                            .flashAttr("bus", createBusDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/buses/new?saved"));

        assertEquals(1, busRepository.count());
    }

    @Test
    @WithMockAdmin
    void getBusesWithoutRoute_shouldReturnPageWithAllBusesWithoutRoute() throws Exception {
        final long routeId = 1L;
        busRepository.save(bus);

        mockMvc.perform(get("/admin/buses/assign")
                            .param("routeId", String.valueOf(routeId)))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/buses/assignToRoute"))
            .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
            .andExpect(model().attribute("page", hasProperty("content", hasItem(
                allOf(
                    hasProperty("driver", is(nullValue())),
                    hasProperty("assignment", is(nullValue())),
                    hasProperty("model", is(bus.getModel())),
                    hasProperty("capacity", is(bus.getCapacity()))
                )
            ))))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockAdmin
    void getBusesWithoutDriver_shouldReturnPageWithAllBusesWithoutDriver() throws Exception {
        final long driverId = 1L;
        busRepository.save(bus);

        mockMvc.perform(get("/admin/buses/assign")
                            .param("driverId", String.valueOf(driverId)))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/buses/assignToDriver"))
            .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
            .andExpect(model().attribute("page", hasProperty("content", hasItem(
                allOf(
                    hasProperty("driver", is(nullValue())),
                    hasProperty("assignment", is(nullValue())),
                    hasProperty("model", is(bus.getModel())),
                    hasProperty("capacity", is(bus.getCapacity()))
                )
            ))))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockAdmin
    void assignBusToRoute_shouldAssignBusToRouteAndRedirectToRequestedPage_whenIdsAreCorrect() throws Exception {
        busRepository.save(bus);
        routeRepository.save(route);

        mockMvc.perform(post("/admin/buses/" + bus.getId() + "/route/" + route.getId())
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?assignedBusToRoute"));

        assertNotNull(busRepository.findById(bus.getId()).get().getRoute());
    }

    @Test
    @WithMockAdmin
    void unassignBusFromRoute_shouldUnassignBusFromRouteAndRedirectToRequestedPage_whenIdIsCorrect() throws Exception {
        routeRepository.save(route);
        bus.setRoute(route);
        busRepository.save(bus);

        mockMvc.perform(post("/admin/buses/" + bus.getId() + "/unassign")
                            .param("redirect", "drivers"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers?unassignedBusFromRoute"));

        assertNull(busRepository.findById(bus.getId()).get().getRoute());
    }
}
