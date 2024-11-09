package com.example.demo.model;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
public class SlotRequest {
     Long stylistId;
     DayOfWeek dayOfWeek;
     LocalDateTime startTime;
     LocalDateTime endTime;
}
