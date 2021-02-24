package ua.kpi.fict.carpark.controller.admin;

import static ua.kpi.fict.carpark.controller.UserInfoProvider.isSuperAdmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.kpi.fict.carpark.dto.request.CreateRouteDto;
import ua.kpi.fict.carpark.service.RouteService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping(value = "/admin/routes")
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    public String getAllRoutes(@PageableDefault(size = 5) Pageable pageable, Model model) {
        log.debug("Request to get page #{} of all routes", pageable.getPageNumber());
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("page", routeService.findAllSorted(pageable));
        return "admin/routes/all";
    }

    @GetMapping(value = "/new")
    public String addRoute(@ModelAttribute("route") CreateRouteDto createRouteDto, Model model) {
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        return "admin/routes/new";
    }

    @PostMapping(value = "/new")
    public String saveRoute(@Valid @ModelAttribute("route") CreateRouteDto createRouteDto,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isSuperAdmin", isSuperAdmin());
            return "admin/routes/new";
        }
        log.debug("Request to save route : {}", createRouteDto);
        routeService.save(createRouteDto);
        return "redirect:/admin/routes/new?saved";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleNumberDuplicate(DataIntegrityViolationException exception) {
        log.debug("Route with such number already exists : ", exception);
        return "redirect:/admin/routes/new?duplicate";
    }
}
