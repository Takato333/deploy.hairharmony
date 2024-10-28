package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.processing.Pattern;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Data
@Entity
@Table(name = "service")
public class ServiceEntity {
    @Column(unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID id;
    
    String name;

    String description;
    String type;

    @Min(value = 0, message = "Price must be positive")
    float price;

    int duration;
    int discount;
    String image;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    float totalPrice;

    @JsonIgnore
    boolean isDeleted = false;

    @OneToMany(mappedBy = "serviceEntity")
    @JsonIgnore
    List<AppointmentDetail> appointmentDetails;

    @OneToMany(mappedBy = "service")
    @JsonIgnore
    Set<Feedback> service_feedbacks;
}
