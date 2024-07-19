package com.eventscheduler.EventScheduler.repository;

import com.eventscheduler.EventScheduler.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    List<User> findAllByEventIdsContaining(String eventId);
}
