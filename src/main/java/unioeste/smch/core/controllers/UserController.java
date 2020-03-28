package unioeste.smch.core.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import unioeste.smch.core.entities.User;
import unioeste.smch.core.repositories.UserRepository;
import unioeste.smch.core.services.UserService;
import unioeste.smch.errors.controllers.ControllerException;
import unioeste.smch.errors.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(
            UserService userService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/users", method = POST)
    public User create(@RequestBody User user) {
        try {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            return userService.save(user);
        }
        catch (ServiceException e) {
            throw new ControllerException(BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "/users", method = PUT)
    public User update(@RequestBody User user) {
        User userBD = userRepository.findOne(user.getId());

        if (!userBD.getPassword().equals(user.getPassword())) {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }
        try {
            return userService.save(user);
        }
        catch (ServiceException e) {
            throw new ControllerException(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/users/{id}", method = DELETE)
    public void delete(@PathVariable Long id) {
        try {
            userService.delete(id);
        }
        catch (ServiceException e) {
            throw new ControllerException(CONFLICT, e.getMessage());
        }
    }

    @RequestMapping(value = "/users", params = {"username"}, method = GET)
    public User findOne(@RequestParam String username) {
        User userResponse = userService.findByUsername(username);
        if (userResponse == null) {
            throw new ControllerException(NOT_FOUND, "Usuário não encontrado");
        }

        return userResponse;
    }

    @RequestMapping(value = "/users/{id}", method = GET)
    public User findOne(@PathVariable Long id) {
        User userResponse = userService.find(id);
        if (userResponse == null) {
            throw new ControllerException(NOT_FOUND, "Usuário não encontrado");
        }

        return userResponse;
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/users", method = GET)
    public List<User> findAll() {
        return userService.findAll();
    }


    @GetMapping("/users/responsible/{id}")
    public List<User> findAllByResponsibleId(@PathVariable Long id) {
        return userService.findAllByResponsibleId(id);
    }

}
