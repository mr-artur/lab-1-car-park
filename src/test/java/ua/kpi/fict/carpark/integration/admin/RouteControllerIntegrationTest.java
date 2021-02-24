package ua.kpi.fict.carpark.integration.admin;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.entity.Route;
import ua.kpi.fict.carpark.integration.TestWithMockedDataWriter;
import ua.kpi.fict.carpark.repository.RouteRepository;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.factories.RouteFactory;

@SpringBootTest
@AutoConfigureMockMvc
class RouteControllerIntegrationTest extends TestWithMockedDataWriter {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteRepository routeRepository;

    private final Route route = RouteFactory.routeInstance();
    private final CreateRouteDto createRouteDto = RouteFactory.createRouteDtoInstance();

    @AfterEach
    void tearDown() {
        routeRepository.deleteAll();
    }

    @Test
    @WithMockAdmin
    void getAllRoutes_shouldReturnPageWithAllRoutes() throws Exception {
        routeRepository.save(route);

        mockMvc.perform(get("/admin/routes"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/routes/all"))
            .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
            .andExpect(model().attribute("page", hasProperty("content", hasItem(
                allOf(
                    hasProperty("id", is(route.getId())),
                    hasProperty("number", is(route.getNumber())),
                    hasProperty("buses", is(Collections.emptyList()))
                )
            ))))
            .andExpect(model().attribute("isSuperAdmin", false));
    }

    @Test
    @WithMockAdmin
    void saveRoute_shouldSaveRoute_whenDtoIsValid() throws Exception {
        mockMvc.perform(post("/admin/routes/new")
                            .flashAttr("route", createRouteDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/routes/new?saved"));

        assertEquals(1, routeRepository.count());
    }

    @Test
    @WithMockAdmin
    void handleNumberDuplicate_shouldReturnRedirectToNewRoutePageWithDuplicateMessage() throws Exception {
        routeRepository.save(route);

        mockMvc.perform(post("/admin/routes/new")
                            .flashAttr("route", createRouteDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/routes/new?duplicate"));

        Assertions.assertEquals(1, routeRepository.count());
    }
}
