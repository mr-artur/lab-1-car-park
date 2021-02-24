package ua.kpi.fict.carpark.controller;

import lombok.Getter;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.kpi.fict.carpark.service.UserService;

public abstract class TestWithMockedSecurityBeans {

    @MockBean
    @Getter
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;
}
