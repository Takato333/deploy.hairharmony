package com.example.demo.repository;

import com.example.demo.entity.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AppointmentDetailRepository extends JpaRepository<AppointmentDetail, UUID> {
//    @Query("SELECT YEAR(a.startTime), MONTH(a.startTime), COUNT(a), SUM(a.price) " +
//            "FROM AppointmentDetail a " +
//            "WHERE a.stylist.id = :stylistId " +
//            "AND a.isDeleted = false " +
//            "GROUP BY YEAR(a.startTime), MONTH(a.startTime)")
//    List<Object[]> calculateMonthlyAppointmentAndEarnings(@Param("stylistId") Long stylistId);
}
