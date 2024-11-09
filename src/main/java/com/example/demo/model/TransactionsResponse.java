package com.example.demo.model;

import com.example.demo.entity.Transactions;
import lombok.Data;

import java.util.List;

@Data
public class TransactionsResponse {
    private List<Transactions> content;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
