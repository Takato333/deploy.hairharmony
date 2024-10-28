package com.example.demo.api;

import com.example.demo.entity.Account;
import com.example.demo.entity.Appointment;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.AppointmentRequest;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.SlotService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AppointmentAPI {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    SlotService slotService;

    @PostMapping
    public ResponseEntity createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Appointment appointment = appointmentService.createAppointment(appointmentRequest);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("payment")
    public ResponseEntity createPayment(@RequestBody AppointmentRequest appointmentRequest) throws Exception {
        String vnPayURL = appointmentService.createUrl(appointmentRequest);
        return ResponseEntity.ok(vnPayURL);
    }

    @PostMapping("transaction")
    public ResponseEntity createTransaction(@RequestParam UUID uuId) throws Exception {
         appointmentService.createTransactions(uuId);
        return ResponseEntity.ok("Successful transaction!");
    }

    @GetMapping
    public ResponseEntity getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAppointmentByIsDeletedFalse();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("{customerId}")
    public ResponseEntity getAllAppointmentsByCustomer(@PathVariable long customerId) {
        Account account = authenticationService.getCurrentAccount();
        List<Appointment> appointments = appointmentRepository.findAppointmentByCustomer(account);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("{appointmentId}")
    public ResponseEntity getAppointment(@PathVariable UUID appointmentId) {
        Appointment appointment = appointmentRepository.findAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<LocalDateTime>> getAvailableTimes(
            @RequestParam Long stylistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<LocalDateTime> availableTimes = slotService.findAvailableSlots(stylistId, date);
        return ResponseEntity.ok(availableTimes);
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable UUID appointmentId, @RequestParam String action) {
        try {
            Appointment updatedAppointment = appointmentService.updateStatusAppointment(appointmentId, action);
            return ResponseEntity.ok(updatedAppointment);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request if the action is invalid
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            // Return 404 Not Found if the appointment does not exist
            return ResponseEntity.notFound().build();
        }
    }


    }
