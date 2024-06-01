package com.eventscheduler.EventScheduler.repository;

import com.eventscheduler.EventScheduler.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
