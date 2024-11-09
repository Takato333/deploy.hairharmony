package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Appointment findAppointmentById(UUID id);
    List<Appointment> findAppointmentByIsDeletedFalse();
    List<Appointment> findAppointmentByCustomer(Account customer);

    @Query("SELECT a FROM Appointment a JOIN a.appointmentDetails ad WHERE ad.stylist.id = :stylistId")
    List<Appointment> findAppointmentsByStylistId(@Param("stylistId") long stylistId);

}
