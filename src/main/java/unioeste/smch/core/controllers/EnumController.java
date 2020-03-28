package unioeste.smch.core.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unioeste.smch.core.models.Topic;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class EnumController {

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/topics", method = GET)
    public Topic[] topics() {
        return Topic.values();
    }
}

