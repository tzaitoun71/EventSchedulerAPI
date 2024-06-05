package com.eventscheduler.EventScheduler.controller;

import com.eventscheduler.EventScheduler.model.User;
import com.eventscheduler.EventScheduler.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new User")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
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
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @Operation(summary = "Delete an existing user")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Adds an event for a User")
    @PostMapping("/{userId}/events/{eventId}")
    public User addEventToUser(@PathVariable String userId, @PathVariable String eventId) {
        return userService.addEventToUser(userId, eventId);
    }
}
