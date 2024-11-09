package com.example.demo.service;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.enums.AppointmentStatus;
import com.example.demo.entity.enums.PaymentEnums;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    public boolean confirmBankingPayment(UUID appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            // Assuming payment method is stored in Appointment as a String or Enum
            if (PaymentEnums.BANKING == appointment.getPayment().getMethod() && !appointment.isPaid()) {
                // Set isPaid to true immediately upon successful banking payment
                appointment.setPaid(true);
                appointmentRepository.save(appointment);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void updateIsPaidStatusForCashAppointments() {
        List<Appointment> appointments = appointmentRepository.findAppointmentByIsDeletedFalse();

        for (Appointment appointment : appointments) {
            // Only check appointments that are marked as DONE and not yet paid
            if (appointment.getStatus() == AppointmentStatus.DONE
                    && !appointment.isPaid()) {

                // Update isPaid to true
                appointment.setPaid(true);
                appointmentRepository.save(appointment);
            }
        }
    }

    // Call this method at the appropriate place in your scheduling or status update logic
    @Scheduled(fixedRate = 60000) // Adjust the timing if necessary
    public void checkAndSetIsPaidForCashPayments() {
        updateIsPaidStatusForCashAppointments();
    }

}
