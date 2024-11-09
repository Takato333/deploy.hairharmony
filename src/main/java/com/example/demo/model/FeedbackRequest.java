package com.example.demo.model;

import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackRequest {
    String content;

    int rating;

    long stylistId;

    UUID serviceId;
}
