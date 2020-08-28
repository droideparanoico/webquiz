package org.hyperskill.webquiz.engine.controller;

import org.hyperskill.webquiz.engine.model.User;
import org.hyperskill.webquiz.engine.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody @Valid User user) {
        userService.registerUser(user);
        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }
}