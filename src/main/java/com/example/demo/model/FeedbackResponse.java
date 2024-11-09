package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackResponse {

    long id;

    String content;

    int rating;

    String email;

    String serviceName;
}
