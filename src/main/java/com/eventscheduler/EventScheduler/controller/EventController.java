package com.eventscheduler.EventScheduler.controller;

import com.eventscheduler.EventScheduler.model.Event;
import com.eventscheduler.EventScheduler.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Event")
@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @Operation(summary = "Create a new Event")
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
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
}
