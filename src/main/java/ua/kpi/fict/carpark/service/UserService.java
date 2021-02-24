package ua.kpi.fict.carpark.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.dto.response.UserDto;
import ua.kpi.fict.carpark.entity.User;

public interface UserService extends UserDetailsService {

    void save(CreateUserDto createUserDto);

    User findByUsername(String username);

    UserDto findLocalizedByUsername(String username);

    Page<UserDto> findNotActivated(Pageable pageable);

    void activate(Long userId);

    void deleteById(Long userId);
}
