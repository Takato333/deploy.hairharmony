package com.example.demo.model;

import lombok.Data;

@Data
public class StylistResponse {
    long id;
    String fullName;
    String description;
    String image;
    int level;
    String sex;
    String token;
}
