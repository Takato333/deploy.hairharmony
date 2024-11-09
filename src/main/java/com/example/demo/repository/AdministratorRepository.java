package com.example.demo.repository;

import com.example.demo.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Administrator findAdministratorById(long id);
    List<Administrator> findAdministratorByIsDeletedFalse();
}
