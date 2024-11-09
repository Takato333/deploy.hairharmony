package com.example.demo.model;

import com.example.demo.entity.RewardClaimed;
import lombok.Data;

import java.util.List;

@Data
public class RewardClaimedResponse {
    private List<RewardClaimed> content;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
