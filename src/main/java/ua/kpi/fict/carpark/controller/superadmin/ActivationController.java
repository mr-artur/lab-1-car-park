package ua.kpi.fict.carpark.controller.superadmin;

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

import ua.kpi.fict.carpark.service.UserService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping(value = "/superadmin/activate")
public class ActivationController {

    private final UserService userService;

    @GetMapping
    public String getNotActivatedUsers(@PageableDefault(size = 5) Pageable pageable, Model model) {
        log.debug("Request to get page #{} of not activated users", pageable.getPageNumber());
        model.addAttribute("page", userService.findNotActivated(pageable));
        return "superadmin/activation/activate";
    }

    @PostMapping(value = "/{userId}")
    public String approveActivation(@PathVariable long userId) {
        log.debug("Request to approve activation of user with id : {}", userId);
        userService.activate(userId);
        return "redirect:/superadmin/activate?activated";
    }

    @PostMapping(value = "/reject/{userId}")
    public String rejectActivation(@PathVariable long userId) {
        log.debug("Request to reject activation of user with id : {}", userId);
        userService.deleteById(userId);
        return "redirect:/superadmin/activate?deleted";
    }
}
