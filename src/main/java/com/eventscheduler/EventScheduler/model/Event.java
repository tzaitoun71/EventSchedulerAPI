package com.eventscheduler.EventScheduler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "events")
public class Event {
    @Id
    private String id;
    private String hostId;
    private String name;
    private LocalDateTime dateTime;
    private Double eventDuration;
    private List<String> userIds;
}
