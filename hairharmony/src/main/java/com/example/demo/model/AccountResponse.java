package com.example.demo.model;

import lombok.Data;

@Data
public class AccountResponse {
    long id;
    String password;
    String fullName;
    String email;
    String phone;
    String role;
    String image;
    String sex;
    float balance;
    String token;

}
