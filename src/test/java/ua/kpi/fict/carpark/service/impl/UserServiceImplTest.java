package ua.kpi.fict.carpark.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.entity.enums.Role;
import ua.kpi.fict.carpark.exception.UserNotFoundException;
import ua.kpi.fict.carpark.util.factories.PageableFactory;
import ua.kpi.fict.carpark.util.factories.UserFactory;
import ua.kpi.fict.carpark.mapper.user.LocalizedUserMapper;
import ua.kpi.fict.carpark.repository.UserRepository;
import ua.kpi.fict.carpark.service.DriverService;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private LocalizedUserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private final long correctId = 1L;
    private final String correctUsername = "admin";
    private final User user = UserFactory.userInstance();

    @Test
    void save_shouldCallSaveMethodOfUserRepository() {
        final CreateUserDto dto = UserFactory.createUserDtoInstance();
        when(passwordEncoder.encode(dto.getPassword())).thenReturn(dto.getPassword());
        when(mapper.toUser(dto)).thenReturn(user);

        userService.save(dto);

        verify(userRepository).save(user);
    }

    @Test
    void findLocalizedByUsername_shouldCallFindByUsernameMethodOfUserRepository_whenUsernameIsCorrect() {
        when(userRepository.findByUsername(correctUsername)).thenReturn(Optional.of(user));

        userService.findLocalizedByUsername(correctUsername);

        verify(userRepository).findByUsername(correctUsername);
    }

    @Test
    void findNotActivated_shouldCallFindEnabledIsFalseMethodOfRepository() {
        final Pageable pageable = PageableFactory.pageableInstance();
        when(userRepository.findByEnabledIsFalse(pageable)).thenReturn(UserFactory.userPageInstance());

        userService.findNotActivated(pageable);

        verify(userRepository).findByEnabledIsFalse(pageable);
    }

    @Test
    void activate_shouldCallActivateMethodOfUserRepository() {
        when(userRepository.findRoleByUserId(correctId)).thenReturn(Role.ROLE_ADMIN);

        userService.activate(correctId);

        verify(userRepository).activateById(correctId);
    }

    @Test
    void activate_shouldCallSaveMethodOfDriverService_whenUserIsDriver() {
        final User userDriver = User.builder().id(correctId).build();
        when(userRepository.findRoleByUserId(correctId)).thenReturn(Role.ROLE_DRIVER);

        userService.activate(correctId);

        verify(driverService).saveAsDriver(userDriver);
    }

    @Test
    void deleteById_shouldCallDeleteByIdMethodOfRepository() {
        userService.deleteById(correctId);

        verify(userRepository).deleteById(correctId);
    }

    @Test
    void findByUsername_shouldCallFindByUsernameMethodOfUserRepository_whenUsernameIsCorrect() {
        when(userRepository.findByUsername(correctUsername)).thenReturn(Optional.of(user));
        userService.findByUsername(correctUsername);

        verify(userRepository).findByUsername(correctUsername);
    }

    @Test
    void findByUsername_shouldThrowEntityNotFoundException_whenUsernameIsIncorrect() {
        final String incorrectUsername = "something";
        when(userRepository.findByUsername(incorrectUsername)).thenReturn(Optional.empty());

        try {
            userService.findByUsername(incorrectUsername);
            fail("Exception was't thrown");
        } catch (Exception e) {
            assertEquals(UserNotFoundException.class, e.getClass());
            assertEquals(String.format("No user found with such username : %s", incorrectUsername), e.getMessage());
        }
    }

    @Test
    void loadUserByUsername_shouldCallFindByUsernameMethodOfUserRepository() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(mapper.toUserDetailsDto(user)).thenReturn(UserFactory.userDetailsDtoInstance());

        userService.loadUserByUsername(user.getUsername());

        verify(userRepository).findByUsername(user.getUsername());
    }
}
