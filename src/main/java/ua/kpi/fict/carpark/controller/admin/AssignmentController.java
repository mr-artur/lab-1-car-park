package ua.kpi.fict.carpark.controller.admin;

import static ua.kpi.fict.carpark.controller.UserInfoProvider.getUsername;
import static ua.kpi.fict.carpark.controller.UserInfoProvider.isSuperAdmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.kpi.fict.carpark.service.AssignmentService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping(value = "/admin/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public String getAllAssignments(@PageableDefault(size = 5) Pageable pageable, Model model) {
        log.debug("Request to get page #{} of all assignments", pageable.getPageNumber());
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("page", assignmentService.findAll(pageable));
        return "admin/assignments/all";
    }

    @PostMapping("/delete/{assignmentId}")
    public String cancelAssignment(@PathVariable long assignmentId) {
        log.debug("Request to cancel assignment with id : {}", assignmentId);
        assignmentService.cancelAssignment(assignmentId);
        return "redirect:/admin/assignments?assignmentCanceled";
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String handleAssignmentNonExistence(EmptyResultDataAccessException exception) {
        log.error("Assignment does not exist : ", exception);
        return "redirect:/admin/assignments?doesNotExist";
    }

    @PostMapping(value = "/driver/{driverId}/bus/{busId}")
    public String openAssignment(@PathVariable long driverId,
                                 @PathVariable long busId,
                                 @RequestParam String redirect) {
        log.debug("Request to open assignment for driver with id : {}, bus with id : {}, initiator with username : {}",
                  driverId, busId, getUsername());
        assignmentService.openAssignment(driverId, busId, getUsername());
        return String.format("redirect:/admin/%s?driverToBusAssignmentOpened", redirect);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleAssignmentWithSameParticipantsExistence(DataIntegrityViolationException exception) {
        log.error("Assignment with same participants already exists : ", exception);
        return "redirect:/admin/assignments?alreadyExists";
    }
}
