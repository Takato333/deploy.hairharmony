package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    String name;

    String image;

    String description;

    int loyaltyPointRequire;

    @JsonIgnore
    boolean isDeleted = false;

    @OneToMany(mappedBy = "reward")
    @JsonIgnore
    List<RewardClaimed> rewards;

}
