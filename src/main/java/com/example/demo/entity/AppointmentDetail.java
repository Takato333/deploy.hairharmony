package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class AppointmentDetail {
    @Column(unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID id;

    LocalDateTime startTime;

    LocalDateTime endTime;

    float price;

    String note;

    @JsonIgnore
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    @JsonIgnore
    Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "service_id")
    ServiceEntity serviceEntity;

    @ManyToOne
    @JoinColumn(name = "stylist_id")
    Stylist stylist;

    @OneToMany(mappedBy = "appointmentDetail")
    @JsonIgnore
    List<Slot> slots;
}
