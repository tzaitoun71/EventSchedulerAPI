package com.eventscheduler.EventScheduler.service;

import com.eventscheduler.EventScheduler.model.User;
import com.eventscheduler.EventScheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
        userRepository.deleteById(id);
    }

    public User addEventToUser(String userId, String eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<String> eventIds = user.getEventIds();
        if (!eventIds.contains(eventId)) {
            eventIds.add(eventId);
            user.setEventIds(eventIds);
            userRepository.save(user);
        }
        return user;
    }
}
