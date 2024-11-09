package com.example.demo.repository;

import com.example.demo.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findManagersById(Long id);
    List<Manager> findManagersByIsDeletedFalse();
}
