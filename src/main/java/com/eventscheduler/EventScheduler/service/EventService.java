package com.eventscheduler.EventScheduler.service;

import com.eventscheduler.EventScheduler.model.Event;
import com.eventscheduler.EventScheduler.model.User;
import com.eventscheduler.EventScheduler.repository.EventRepository;
import com.eventscheduler.EventScheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Transactional
    public Event updateEvent(String id, Event event) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        existingEvent.setName(event.getName());
        existingEvent.setDateTime(event.getDateTime());
        existingEvent.setUserIds(event.getUserIds());
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(String id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }

        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        List<User> users = userRepository.findAllByEventIdsContaining(id);

        for (User user : users) {
            user.getEventIds().remove(id);
            userRepository.save(user);
        }
        eventRepository.deleteById(id);
    }

    public List<Event> getEventsByHostId(String hostId) {
            return eventRepository.findAllByHostId(hostId);
    }

    @Transactional
    public Event addUserToEvent(String eventId, String userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Add user to event's user list if not already present
        List<String> userIds = event.getUserIds();
        if (!userIds.contains(userId)) {
            userIds.add(userId);
            event.setUserIds(userIds);
        }

        // Add event to user's event list if not already present
        List<String> eventIds = user.getEventIds();
        if (!eventIds.contains(eventId)) {
            eventIds.add(eventId);
            user.setEventIds(eventIds);
        }

        userRepository.save(user);
        return eventRepository.save(event);
    }
}
