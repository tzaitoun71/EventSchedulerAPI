package com.eventscheduler.EventScheduler.service;

import com.eventscheduler.EventScheduler.model.Event;
import com.eventscheduler.EventScheduler.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(Event event) {
        if (eventRepository.findByNameAndDateTime(event.getName(), event.getDateTime()).isPresent()) {
            throw new RuntimeException("Event with name " + event.getName() + " at date and time " + event.getDateTime() + " already exists");
        }
        return eventRepository.save(event);
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(String id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event updateEvent(String id, Event event) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        existingEvent.setName(event.getName());
        existingEvent.setDateTime(event.getDateTime());
        existingEvent.setUserIds(event.getUserIds());
        return eventRepository.save(event);
    }

    public void deleteEvent(String id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }
}
