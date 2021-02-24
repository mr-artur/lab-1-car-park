package ua.kpi.fict.carpark.controller;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.entity.enums.Role;
import ua.kpi.fict.carpark.util.annotations.WithMockAdmin;
import ua.kpi.fict.carpark.util.annotations.WithMockDriver;
import ua.kpi.fict.carpark.util.annotations.WithMockSuperAdmin;
import ua.kpi.fict.carpark.util.factories.UserFactory;

@WebMvcTest(controllers = SecurityController.class)
class SecurityControllerTest extends TestWithMockedSecurityBeans {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_shouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("security/login"));
    }

    @Test
    @WithAnonymousUser
    void login_shouldRedirectAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/success"));
    }

    @Test
    void getRegisterPage_shouldReturnRegisterPageWithAppropriateAttributes() throws Exception {
        mockMvc.perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));
    }

    @Test
    @WithMockUser
    void login_shouldNotWorkForAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/register"))
            .andExpect(status().isForbidden());
    }

    @Test
    void newUser_shouldCallSaveMethodOfUserService_whenDtoIsValid() throws Exception {
        final CreateUserDto createUserDto = UserFactory.createUserDtoInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", createUserDto))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/login?registered"));

        verify(getUserService()).save(createUserDto);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenUsernameIsNull() throws Exception {
        final CreateUserDto dtoWithoutUsername = UserFactory.createUserDtoWithoutUsernameInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithoutUsername))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "username"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithoutUsername);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenUsernameDoesNotMatchPattern() throws Exception {
        final CreateUserDto dtoWithIncorrectUsername = UserFactory.createUserDtoWithIncorrectUsernameInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithIncorrectUsername))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "username"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithIncorrectUsername);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenPasswordIsNull() throws Exception {
        final CreateUserDto dtoWithoutPassword = UserFactory.createUserDtoWithoutPasswordInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithoutPassword))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "password"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithoutPassword);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenPasswordDoesNotMatchPattern() throws Exception {
        final CreateUserDto dtoWithIncorrectPassword = UserFactory.createUserDtoWithIncorrectPasswordInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithIncorrectPassword))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "password"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithIncorrectPassword);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenRoleIsNull() throws Exception {
        final CreateUserDto dtoWithoutRole = UserFactory.createUserDtoWithoutRoleInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithoutRole))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "role"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithoutRole);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenFirstNameIsNull() throws Exception {
        final CreateUserDto dtoWithoutFirstName = UserFactory.createUserDtoWithoutFirstNameInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithoutFirstName))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "firstName"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithoutFirstName);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenFirstNameDoesNotMatchPattern() throws Exception {
        final CreateUserDto dtoWithIncorrectFirstName = UserFactory.createUserDtoWithIncorrectFirstNameInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithIncorrectFirstName))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "firstName"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithIncorrectFirstName);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenFirstNameNativeIsNull() throws Exception {
        final CreateUserDto dtoWithoutFirstNameNative = UserFactory.createUserDtoWithoutFirstNameNativeInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithoutFirstNameNative))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "firstNameNative"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithoutFirstNameNative);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenFirstNameNativeDoesNotMatchPattern() throws Exception {
        final CreateUserDto dtoWithIncorrectFirstNameNative = UserFactory.createUserDtoWithIncorrectFirstNameNativeInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithIncorrectFirstNameNative))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "firstNameNative"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithIncorrectFirstNameNative);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenLastNameIsNull() throws Exception {
        final CreateUserDto dtoWithoutLastName = UserFactory.createUserDtoWithoutLastNameInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithoutLastName))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "lastName"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithoutLastName);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenLastNameDoesNotMatchPattern() throws Exception {
        final CreateUserDto dtoWithIncorrectLastName = UserFactory.createUserDtoWithIncorrectLastNameInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithIncorrectLastName))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "lastName"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithIncorrectLastName);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenLastNameNativeIsNull() throws Exception {
        final CreateUserDto dtoWithoutLastNameNative = UserFactory.createUserDtoWithoutLastNameNativeInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithoutLastNameNative))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "lastNameNative"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithoutLastNameNative);
    }

    @Test
    void newUser_shouldNotCallSaveMethodOfUserService_whenLastNameNativeDoesNotMatchPattern() throws Exception {
        final CreateUserDto dtoWithIncorrectLastNameNative = UserFactory.createUserDtoWithIncorrectLastNameNativeInstance();

        mockMvc.perform(post("/register")
                            .flashAttr("user", dtoWithIncorrectLastNameNative))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("user", "lastNameNative"))
            .andExpect(view().name("security/register"))
            .andExpect(model().attribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER)));

        verify(getUserService(), never()).save(dtoWithIncorrectLastNameNative);
    }

    @Test
    @WithMockUser
    void newUser_shouldNotWorkForAuthenticatedUser() throws Exception {
        mockMvc.perform(post("/register"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockSuperAdmin
    void successRedirect_shouldRedirectSuperAdminToAppropriatePage() throws Exception {
        mockMvc.perform(get("/success"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/superadmin/activate"));
    }

    @Test
    @WithMockAdmin
    void successRedirect_shouldRedirectAdminToAppropriatePage() throws Exception {
        mockMvc.perform(get("/success"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/drivers"));
    }

    @Test
    @WithMockDriver
    void successRedirect_shouldRedirectDriverToAppropriatePage() throws Exception {
        mockMvc.perform(get("/success"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/driver/home"));
    }
}
