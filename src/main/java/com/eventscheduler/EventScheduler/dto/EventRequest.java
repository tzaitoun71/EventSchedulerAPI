package com.eventscheduler.EventScheduler.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {
    @NotBlank
    private String name;
    @NotBlank
    private LocalDateTime dateTime;
    @NotBlank
    private Double eventDuration;
}
