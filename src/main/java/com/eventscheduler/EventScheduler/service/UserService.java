package com.eventscheduler.EventScheduler.service;

import com.eventscheduler.EventScheduler.model.Event;
import com.eventscheduler.EventScheduler.model.User;
import com.eventscheduler.EventScheduler.repository.EventRepository;
import com.eventscheduler.EventScheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not found");

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(String id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        // Updating fields
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEventIds(user.getEventIds());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        List<Event> events = eventRepository.findAllByUserIdsContains(id);

        for (Event event : events) {
            event.getUserIds().remove(id);
            eventRepository.save(event);
        }
        userRepository.deleteById(id);
    }

    public User removeEvent(String eventId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<String> eventIds = user.getEventIds();
        if (eventIds.contains(eventId)) {
            eventIds.remove(eventId);
            user.setEventIds(eventIds);
            userRepository.save(user);
        }
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}