package com.example.demo.model;

import com.example.demo.entity.enums.StylistStatus;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
public class SlotResponse {
     Long id;
     DayOfWeek dayOfWeek;
     LocalDateTime startTime;
     LocalDateTime endTime;
     StylistStatus status;
     Long stylistId;
     String stylistName;

}
