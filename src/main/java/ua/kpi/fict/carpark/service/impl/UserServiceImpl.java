package ua.kpi.fict.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.dto.response.UserDto;
import ua.kpi.fict.carpark.entity.enums.Role;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.exception.UserNotFoundException;
import ua.kpi.fict.carpark.mapper.user.LocalizedUserMapper;
import ua.kpi.fict.carpark.repository.UserRepository;
import ua.kpi.fict.carpark.service.DriverService;
import ua.kpi.fict.carpark.service.UserService;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DriverService driverService;
    private final LocalizedUserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(CreateUserDto createUserDto) {
        log.debug("Request to save user : {}", createUserDto);
        User user = mapper.toUser(createUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.debug("Saved user : {}", user);
    }

    @Override
    public UserDto findLocalizedByUsername(String username) {
        return mapper.toDto(findByUsername(username));
    }

    @Override
    public Page<UserDto> findNotActivated(Pageable pageable) {
        return userRepository.findByEnabledIsFalse(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void activate(Long userId) {
        log.debug("Request to activate user with id : {}", userId);
        userRepository.activateById(userId);
        saveAsDriverIfNecessary(userId);
        log.debug("Activated user with id : {}", userId);
    }

    private void saveAsDriverIfNecessary(long userId) {
        if (userRepository.findRoleByUserId(userId).equals(Role.ROLE_DRIVER)) {
            driverService.saveAsDriver(User.builder().id(userId).build());
        }
    }

    @Override
    public void deleteById(Long userId) {
        log.debug("Request to delete user with id : {}", userId);
        userRepository.deleteById(userId);
        log.debug("Deleted user with id : {}", userId);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(String.format("No user found with such username : %s",
                                                                       username)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(mapper::toUserDetailsDto)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with such username : %s",
                                                                           username)));
    }
}
