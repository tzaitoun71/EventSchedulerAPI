package com.eventscheduler.EventScheduler.controller;

import com.eventscheduler.EventScheduler.dto.UserRequest;
import com.eventscheduler.EventScheduler.dto.SigninRequest;
import com.eventscheduler.EventScheduler.model.User;
import com.eventscheduler.EventScheduler.service.UserService;
import com.eventscheduler.EventScheduler.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Signup a new User")
    @PostMapping("/signup")
    public User signup(@RequestBody UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEventIds(new ArrayList<>());

        return userService.createUser(user);
    }

    @Operation(summary = "Sign in a User")
    @PostMapping("/signin")
    public String signin(@RequestBody SigninRequest signinRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        final UserDetails userDetails = userService.loadUserByUsername(signinRequest.getUsername());
        return jwtUtil.generateToken(userDetails);
    }


    @Operation(summary = "Get all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Update an existing user")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @Valid @RequestBody UserRequest userRequest) {
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        existingUser.setUsername(userRequest.getUsername());
        existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        existingUser.setFirstName(userRequest.getFirstName());
        existingUser.setLastName(userRequest.getLastName());

        return userService.updateUser(id, existingUser);
    }

    @Operation(summary = "Delete an existing user")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Remove an event from a User's event list")
    @DeleteMapping("/{userId}/events/{eventId}")
    public User removeEvent(@PathVariable String eventId, @PathVariable String userId) {
        return userService.removeEvent(eventId, userId);
    }
}