package com.example.demo.model;

import lombok.Data;

@Data
public class AdministratorResponse {
    long id;
    String fullName;
    String image;
    String sex;
    String token;
}
