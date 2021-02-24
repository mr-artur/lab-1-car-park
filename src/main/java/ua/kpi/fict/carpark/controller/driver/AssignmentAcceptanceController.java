package ua.kpi.fict.carpark.controller.driver;

import static ua.kpi.fict.carpark.controller.UserInfoProvider.getUsername;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ua.kpi.fict.carpark.exception.AssignmentNotFoundException;
import ua.kpi.fict.carpark.service.AssignmentService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping(value = "/driver/assignments")
public class AssignmentAcceptanceController {

    private final AssignmentService assignmentService;

    @GetMapping
    public String getAssignment(Model model) {
        log.debug("Request to get assignment for driver with username : {}", getUsername());
        model.addAttribute("assignment", assignmentService.findByDriverUsername(getUsername()));
        return "driver/assignment";
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ModelAndView handleAssignmentAbsence() {
        ModelAndView modelAndView = new ModelAndView("driver/assignment");
        modelAndView.addObject("notFound", true);
        return modelAndView;
    }

    @PostMapping(value = "/accept/{assignmentId}")
    public String acceptAssignment(@PathVariable long assignmentId) {
        log.debug("Request to accept assignment with id : {}", assignmentId);
        assignmentService.acceptAssignment(assignmentId);
        return "redirect:/driver/job?assignmentAccepted";
    }
}
