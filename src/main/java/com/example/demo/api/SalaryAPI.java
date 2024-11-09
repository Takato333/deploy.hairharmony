package com.example.demo.api;

import com.example.demo.model.TransactionsResponse;
import com.example.demo.service.SalaryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salary")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
@PreAuthorize("hasAuthority('MANAGER')")
public class SalaryAPI {

    @Autowired
    SalaryService salaryService;

    @GetMapping
    public ResponseEntity getAllTransactions(@RequestParam int page, @RequestParam(defaultValue = "10") int size) {
        TransactionsResponse transactionsResponse = salaryService.getAllTransactions(page, size);
        return ResponseEntity.ok(transactionsResponse);
    }

}
