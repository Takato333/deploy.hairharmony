package com.example.demo.repository;

import com.example.demo.entity.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentDetailRepository extends JpaRepository<AppointmentDetail, UUID> {
    @Query("SELECT COUNT(ad.id) FROM AppointmentDetail ad " +
            "WHERE ad.stylist.id = :stylistId AND ad.isDeleted = false AND ad.startTime >= :startOfMonth")
    int countBookedAppointmentsByStylistId(@Param("stylistId") Long stylistId, @Param("startOfMonth") LocalDateTime startOfMonth);
}
