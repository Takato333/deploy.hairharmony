package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Appointment findAppointmentById(UUID id);
    List<Appointment> findAppointmentByIsDeletedFalse();
    List<Appointment> findAppointmentByCustomer(Account customer);

}
