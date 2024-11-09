package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    String content;

    int rating;

    @JsonIgnore
    boolean isDeleted = false;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_from")
    Account customer;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "stylist_id")
    Account stylist;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "service_id")
    ServiceEntity service;

}