package com.eventscheduler.EventScheduler.controller;


import com.eventscheduler.EventScheduler.model.Event;
import com.eventscheduler.EventScheduler.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @GetMapping
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable String id) {
        return eventService.getEventById(id);
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable String id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }
}
