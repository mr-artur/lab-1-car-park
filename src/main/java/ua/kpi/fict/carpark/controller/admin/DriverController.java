package ua.kpi.fict.carpark.controller.admin;

import static ua.kpi.fict.carpark.controller.UserInfoProvider.isSuperAdmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.kpi.fict.carpark.service.DriverService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping(value = "/admin/drivers")
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public String getAllDrivers(@PageableDefault(size = 5) Pageable pageable, Model model) {
        log.debug("Request to get page #{} of all drivers", pageable.getPageNumber());
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("page", driverService.findAll(pageable));
        return "admin/drivers/all";
    }

    @GetMapping(value = "/assign")
    public String getDriversWithoutBusAndOpenedAssignment(@PageableDefault(size = 5) Pageable pageable,
                                                          @RequestParam long busId,
                                                          Model model) {
        log.debug("Request to get page #{} of drivers without bus and opened assignment", pageable.getPageNumber());
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("busId", busId);
        model.addAttribute("page", driverService.findAllWithoutBusAndOpenedAssignment(pageable));
        return "admin/drivers/assignToBus";
    }

    @PostMapping(value = "/{driverId}/unassign")
    public String unassignDriverFromBus(@PathVariable long driverId, @RequestParam String redirect) {
        log.debug("Request to unassign driver with id : {} from bus", driverId);
        driverService.unassignDriverFromBus(driverId);
        return String.format("redirect:/admin/%s?unassignedDriverFromBus", redirect);
    }
}
