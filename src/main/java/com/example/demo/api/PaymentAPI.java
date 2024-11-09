package com.example.demo.api;

import com.example.demo.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("*")
@SecurityRequirement(name = "api")

public class PaymentAPI {
    @Autowired
    private PaymentService paymentService;

    @PutMapping("/confirm-banking")
    public ResponseEntity<String> confirmBankingPayment(@RequestParam UUID appointmentId) {
        boolean success = paymentService.confirmBankingPayment(appointmentId);

        if (success) {
            return ResponseEntity.ok("Xác nhận giao dịch online thành công!");
        } else {
            return ResponseEntity.badRequest().body("Thất bại khi xác nhận giao dịch!");
        }
    }
}
