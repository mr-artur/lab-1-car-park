package ua.kpi.fict.carpark.controller.driver;

import static ua.kpi.fict.carpark.controller.UserInfoProvider.getUsername;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.kpi.fict.carpark.service.DriverService;
import ua.kpi.fict.carpark.service.UserService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping(value = "/driver")
public class PageController {

    private final UserService userService;

    private final DriverService driverService;

    @GetMapping(value = "/home")
    public String getHomePage(Model model) {
        log.debug("Request to get home page with user info for driver with username : {}", getUsername());
        model.addAttribute("user", userService.findLocalizedByUsername(getUsername()));
        return "driver/home";
    }

    @GetMapping(value = "/job")
    public String getJobPage(Model model) {
        log.debug("Request to get job page with info for driver with username : {}", getUsername());
        model.addAttribute("driver", driverService.findByUsername(getUsername()));
        return "driver/job";
    }
}
