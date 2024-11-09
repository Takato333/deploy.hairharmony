package com.example.demo.repository;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
//        Payment findByPaymentId(Long paymentId);
//        Payment findByAppointment(Appointment appointment);
}
