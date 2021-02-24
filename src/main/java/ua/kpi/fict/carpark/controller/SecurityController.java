package ua.kpi.fict.carpark.controller;

import static ua.kpi.fict.carpark.controller.UserInfoProvider.isAdmin;
import static ua.kpi.fict.carpark.controller.UserInfoProvider.isAuthenticated;
import static ua.kpi.fict.carpark.controller.UserInfoProvider.isSuperAdmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.entity.enums.Role;
import ua.kpi.fict.carpark.service.UserService;

import javax.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Controller
public class SecurityController {

    private final UserService userService;

    @GetMapping(value = "/login")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:/success";
        }
        return "security/login";
    }

    @GetMapping(value = "/register")
    public String getRegisterPage(@ModelAttribute("user") CreateUserDto createUserDto, Model model) {
        model.addAttribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER));
        return "security/register";
    }

    @PostMapping(value = "/register")
    public String newUser(@Valid @ModelAttribute("user") CreateUserDto createUserDto,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", List.of(Role.ROLE_ADMIN, Role.ROLE_DRIVER));
            return "security/register";
        }
        log.debug("Request to create new user : {}", createUserDto);
        userService.save(createUserDto);
        return "redirect:/login?registered";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleUsernameDuplicate(DataIntegrityViolationException exception) {
        log.debug("User with such username already exists : ", exception);
        return "redirect:/register?duplicate";
    }

    @RequestMapping("/success")
    public String successRedirect() {
        if (isSuperAdmin()) {
            return "redirect:/superadmin/activate";
        } else if (isAdmin()) {
            return "redirect:/admin/drivers";
        }
        return "redirect:/driver/home";
    }
}
