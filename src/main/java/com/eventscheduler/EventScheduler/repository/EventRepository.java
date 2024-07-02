package com.eventscheduler.EventScheduler.repository;

import com.eventscheduler.EventScheduler.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findByNameAndDateTime(String name, LocalDateTime dateTime);

    List<Event> findAllByHostId(String hostId);
}
