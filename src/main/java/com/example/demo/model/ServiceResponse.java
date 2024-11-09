package com.example.demo.model;

import lombok.Data;

import java.util.UUID;

@Data
public class ServiceResponse {
    UUID id;
    String name;
    String description;
    String type;
    String image;
    int duration;
    float price;
    int discount;
    float totalPrice;

}
