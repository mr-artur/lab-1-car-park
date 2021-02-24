package ua.kpi.fict.carpark.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.repository.UserRepository;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityControllerIntegrationTest extends TestWithMockedDataWriter {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final CreateUserDto createUserDto = UserFactory.createUserDtoInstance();

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void newUser_shouldSaveNewUser_whenDtoIsValid() throws Exception {
        mockMvc.perform(post("/register")
                            .flashAttr("user", createUserDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/login?registered"));

        assertEquals(1, userRepository.count());
    }

    @Test
    void handleUsernameDuplicate_shouldReturnRedirectToRegisterPageWithDuplicateMessage() throws Exception {
        userRepository.save(UserFactory.userInstance());

        mockMvc.perform(post("/register")
                            .flashAttr("user", createUserDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/register?duplicate"));

        assertEquals(1, userRepository.count());
    }
}
