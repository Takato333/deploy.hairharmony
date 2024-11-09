package com.example.demo.model;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentDetail;
import lombok.Data;

import java.util.List;

@Data
public class AppointmentRequest {
    List<AppointmentDetailRequest> details;
}
