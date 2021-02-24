package ua.kpi.fict.carpark.util.factories;

import org.springframework.data.domain.Page;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.dto.response.UserDto;
import ua.kpi.fict.carpark.dto.security.UserDetailsDto;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.entity.enums.Role;

public class UserFactory {

    public static CreateUserDto createUserDtoInstance() {
        return CreateUserDto.builder()
            .username("karimlegoida")
            .password("1234567a")
            .firstName("Karim")
            .firstNameNative("Карим")
            .lastName("Legoida")
            .lastNameNative("Легойда")
            .role(Role.ROLE_DRIVER)
            .build();
    }

    public static User userInstance() {
        return User.builder()
            .username("karimlegoida")
            .password("1234567a")
            .firstName("Karim")
            .firstNameNative("Карим")
            .lastName("Legoida")
            .lastNameNative("Легойда")
            .role(Role.ROLE_DRIVER)
            .build();
    }

    public static User activateUser(User user) {
        return user.toBuilder()
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .enabled(true)
            .build();
    }

    public static User activatedUserInstance() {
        return activateUser(userInstance());
    }

    public static User activatedAdminInstance() {
        User admin = User.builder()
            .username("genamytrofanov")
            .password("1234567a")
            .firstName("Gennadiy")
            .firstNameNative("Геннадий")
            .lastName("Mytrofanov")
            .lastNameNative("Митрофанов")
            .role(Role.ROLE_ADMIN)
            .build();
        return activateUser(admin);
    }

    public static User activatedSuperAdminInstance() {
        User superAdmin = User.builder()
            .username("superadmin123")
            .password("1234567a")
            .firstName("Maksym")
            .firstNameNative("Максим")
            .lastName("Dovgopol")
            .lastNameNative("Довгопол")
            .role(Role.ROLE_SUPERADMIN)
            .build();
        return activateUser(superAdmin);
    }

    public static Page<User> userPageInstance() {
        return Page.empty();
    }

    public static UserDetailsDto userDetailsDtoInstance() {
        return new UserDetailsDto();
    }

    public static Page<UserDto> userDtosPageInstance() {
        return Page.empty();
    }

    public static UserDto userDtoInstance() {
        return UserDto.builder()
            .id(1L)
            .build();
    }

    public static CreateUserDto createUserDtoWithoutUsernameInstance() {
        return createUserDtoInstance().toBuilder()
            .username(null)
            .build();
    }

    public static CreateUserDto createUserDtoWithIncorrectUsernameInstance() {
        return createUserDtoInstance().toBuilder()
            .username("123")
            .build();
    }

    public static CreateUserDto createUserDtoWithoutPasswordInstance() {
        return createUserDtoInstance().toBuilder()
            .password(null)
            .build();
    }

    public static CreateUserDto createUserDtoWithIncorrectPasswordInstance() {
        return createUserDtoInstance().toBuilder()
            .password("123")
            .build();
    }

    public static CreateUserDto createUserDtoWithoutRoleInstance() {
        return createUserDtoInstance().toBuilder()
            .role(null)
            .build();
    }

    public static CreateUserDto createUserDtoWithoutFirstNameInstance() {
        return createUserDtoInstance().toBuilder()
            .firstName(null)
            .build();
    }

    public static CreateUserDto createUserDtoWithIncorrectFirstNameInstance() {
        return createUserDtoInstance().toBuilder()
            .firstName("123")
            .build();
    }

    public static CreateUserDto createUserDtoWithoutFirstNameNativeInstance() {
        return createUserDtoInstance().toBuilder()
            .firstNameNative(null)
            .build();
    }

    public static CreateUserDto createUserDtoWithIncorrectFirstNameNativeInstance() {
        return createUserDtoInstance().toBuilder()
            .firstNameNative("123")
            .build();
    }

    public static CreateUserDto createUserDtoWithoutLastNameInstance() {
        return createUserDtoInstance().toBuilder()
            .lastName(null)
            .build();
    }

    public static CreateUserDto createUserDtoWithIncorrectLastNameInstance() {
        return createUserDtoInstance().toBuilder()
            .lastName("123")
            .build();
    }

    public static CreateUserDto createUserDtoWithoutLastNameNativeInstance() {
        return createUserDtoInstance().toBuilder()
            .lastNameNative(null)
            .build();
    }

    public static CreateUserDto createUserDtoWithIncorrectLastNameNativeInstance() {
        return createUserDtoInstance().toBuilder()
            .lastNameNative("123")
            .build();
    }
}
