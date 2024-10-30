package com.example.demo.entity;

import com.example.demo.entity.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Column(unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID id;

    Date date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    float totalPrice;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;

    @JsonIgnore
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    Account customer;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    boolean loyaltyPointsAwarded = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    boolean reminderSent = false;


    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<AppointmentDetail> appointmentDetails;

    @OneToOne(mappedBy = "appointment")
    @ToString.Exclude
    @JsonIgnore
    Payment payment;


}
