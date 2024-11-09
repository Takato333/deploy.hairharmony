package com.example.demo.repository;

import com.example.demo.entity.Stylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StylistRepository extends JpaRepository<Stylist, Long> {
        Stylist findStylistByIdAndIsDeletedFalse(Long id);
        List<Stylist> findStylistsByLevel(int level);
        List<Stylist> findStylistsByIsDeletedFalse();
}
