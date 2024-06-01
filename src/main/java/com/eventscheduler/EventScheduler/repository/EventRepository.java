package com.eventscheduler.EventScheduler.repository;

import com.eventscheduler.EventScheduler.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

}
