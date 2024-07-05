package com.eventscheduler.EventScheduler.controller;

import com.eventscheduler.EventScheduler.dto.EventRequest;
import com.eventscheduler.EventScheduler.model.Event;
import com.eventscheduler.EventScheduler.model.User;
import com.eventscheduler.EventScheduler.service.EventService;
import com.eventscheduler.EventScheduler.service.UserService;
import com.eventscheduler.EventScheduler.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Event")
@RestController
@RequestMapping("/events")
@SecurityRequirement(name = "bearerAuth")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new Event")
    @PostMapping
    public Event createEvent(@RequestBody EventRequest eventRequest, HttpServletRequest request) {
        String token = extractJwtFromRequest(request);
        String username = jwtUtil.extractUsername(token);
        User user = userService.findByUsername(username);

        Event event = new Event();
        if (user != null) event.setHostId(user.getId());
        event.setName(eventRequest.getName());
        event.setDateTime(eventRequest.getDateTime());
        event.setEventDuration(eventRequest.getEventDuration());
        event.setUserIds(new ArrayList<>());

        return eventService.createEvent(event);
    }

    @Operation(summary = "Get all events")
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getEvents();
    }

    @Operation(summary = "Get event by id")
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable String id) {
        return eventService.getEventById(id);
    }

    @Operation(summary = "Update an existing event")
    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable String id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @Operation(summary = "Delete an existing event")
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

    @Operation(summary = "Adds a user to an Event")
    @PostMapping("/{eventId}/users/{userId}")
    public Event addUserToEvent(@PathVariable String eventId, @PathVariable String userId) {
        return eventService.addUserToEvent(eventId, userId);
    }

    @Operation(summary = "Gets all events depending on User ID")
    @GetMapping("/{hostId}")
    public List<Event> getEventsByHostId(@PathVariable String hostId) {
        return eventService.getEventsByHostId(hostId);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
