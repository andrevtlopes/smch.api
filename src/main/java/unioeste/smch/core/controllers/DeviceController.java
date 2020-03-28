package unioeste.smch.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unioeste.smch.core.entities.Device;
import unioeste.smch.core.services.DeviceService;
import unioeste.smch.errors.controllers.ControllerException;
import unioeste.smch.errors.services.ServiceException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class DeviceController {
    private DeviceService deviceService;

    @Autowired
    public DeviceController(
            DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/devices", method = POST)
    public Device create(@RequestBody Device device) {
        try {
            return deviceService.save(device);
        }
        catch (ServiceException e) {
            throw new ControllerException(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/devices", method = PUT)
    public Device update(@RequestBody Device device) {
        try {
            return deviceService.save(device);
        }
        catch (ServiceException e) {
            throw new ControllerException(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/devices/{id}", method = DELETE)
    public void delete(@PathVariable Long id) {
        try {
            deviceService.delete(id);
        }
        catch (ServiceException e) {
            throw new ControllerException(CONFLICT, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/devices/{id}", method = GET)
    public Device findOne(@PathVariable Long id) {
        Device deviceResponse = deviceService.find(id);
        if (deviceResponse == null) {
            throw new ControllerException(NOT_FOUND, "Usuário não encontrado");
        }

        return deviceResponse;
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/devices", method = GET)
    public List<Device> findAll() {
        return deviceService.findAll();
    }
}
