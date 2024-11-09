package com.example.demo.model;

import lombok.Data;

@Data
public class ManagerResponse {
    long id;
    String fullName;
    String image;
    String sex;
    String token;
}
