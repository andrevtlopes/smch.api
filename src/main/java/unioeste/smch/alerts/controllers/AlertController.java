package unioeste.smch.alerts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import unioeste.smch.alerts.entities.Alert;
import unioeste.smch.alerts.services.AlertService;
import unioeste.smch.errors.controllers.ControllerException;
import unioeste.smch.errors.services.ServiceException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
public class AlertController {
    private AlertService alertService;

    @Autowired
    public AlertController(
            AlertService alertService) {
        this.alertService = alertService;
    }

    @RequestMapping(value = "/alerts", method = PUT)
    public Alert update(@RequestBody Alert alert) {
        try {
            return alertService.save(alert);
        }
        catch (ServiceException e) {
            throw new ControllerException(BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "/alerts/verified", method = PUT)
    public Alert setVerified(@RequestBody Long id) {
        try {
            return alertService.setVerified(id);
        }
        catch (ServiceException e) {
            throw new ControllerException(BAD_REQUEST, e.getMessage());
        }
    }


    @RequestMapping(value = "/alerts", method = GET)
    public List<Alert> findAll(@RequestParam Optional<String> username) {
        if (username.isPresent()) {
            return alertService.findAllByUsername(username.get());
        }
        return alertService.findAllByVerified(false);
    }
}
