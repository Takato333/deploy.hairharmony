package com.example.demo.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class AppointmentDetailRequest {
    UUID serviceId;
    long stylistId;
    LocalDateTime startTime;
    String note;

}
