package com.example.demo.model;

import com.example.demo.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StaffResponse {
    long id;
    String fullName;
    String image;
    String sex;
    String token;
}
