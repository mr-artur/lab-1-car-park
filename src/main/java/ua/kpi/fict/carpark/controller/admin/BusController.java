package ua.kpi.fict.carpark.controller.admin;

import static ua.kpi.fict.carpark.controller.UserInfoProvider.isSuperAdmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.kpi.fict.carpark.dto.request.CreateBusDto;
import ua.kpi.fict.carpark.service.BusService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping(value = "/admin/buses")
public class BusController {

    private final BusService busService;

    @GetMapping
    public String getAllBuses(@PageableDefault(size = 5) Pageable pageable, Model model) {
        log.debug("Request to get page #{} of all buses", pageable.getPageNumber());
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("page", busService.findAll(pageable));
        return "admin/buses/all";
    }

    @GetMapping(value = "/new")
    public String addBus(@ModelAttribute("bus") CreateBusDto createBusDto, Model model) {
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        return "admin/buses/new";
    }

    @PostMapping(value = "/new")
    public String saveBus(@Valid @ModelAttribute("bus") CreateBusDto createBusDto,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isSuperAdmin", isSuperAdmin());
            return "admin/buses/new";
        }
        log.debug("Request to save bus : {}", createBusDto);
        busService.save(createBusDto);
        return "redirect:/admin/buses/new?saved";
    }

    @GetMapping(value = "/assign", params = {"routeId"})
    public String getBusesWithoutRoute(@PageableDefault(size = 5) Pageable pageable,
                                       @RequestParam long routeId,
                                       Model model) {
        log.debug("Request to get page #{} of buses without route", pageable.getPageNumber());
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("routeId", routeId);
        model.addAttribute("page", busService.findAllWithoutRoute(pageable));
        return "admin/buses/assignToRoute";
    }

    @GetMapping(value = "/assign", params = {"driverId"})
    public String getBusesWithoutDriver(@PageableDefault(size = 5) Pageable pageable,
                                        @RequestParam long driverId,
                                        Model model) {
        log.debug("Request to get page #{} of buses without driver", pageable.getPageNumber());
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("driverId", driverId);
        model.addAttribute("page", busService.findAllWithoutDriverAndOpenedAssignment(pageable));
        return "admin/buses/assignToDriver";
    }

    @PostMapping(value = "/{busId}/route/{routeId}")
    public String assignBusToRoute(@PathVariable long busId,
                                   @PathVariable long routeId,
                                   @RequestParam String redirect) {
        log.debug("Request to assign bus with id : {} to route with id : {}", busId, routeId);
        busService.assignBusToRoute(busId, routeId);
        return String.format("redirect:/admin/%s?assignedBusToRoute", redirect);
    }

    @PostMapping(value = "/{busId}/unassign")
    public String unassignBusFromRoute(@PathVariable long busId, @RequestParam String redirect) {
        log.debug("Request to unassign bus with id : {} from route", busId);
        busService.unassignBusFromRoute(busId);
        return String.format("redirect:/admin/%s?unassignedBusFromRoute", redirect);
    }
}
