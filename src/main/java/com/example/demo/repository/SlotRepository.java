package com.example.demo.repository;

import com.example.demo.entity.Slot;
import com.example.demo.entity.Stylist;
import com.example.demo.entity.enums.StylistStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    Slot findSlotById(long id);
    List<Slot> findBySlotStylistIdAndIsDeletedFalse(Long stylistId);;
    List<Slot> findByStylistStatus(StylistStatus stylistStatus);
}
