package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {

    //tim sccount by username
    Account findAccountByPhone(String phone);

    Account findAccountById(long id);

    Account findAccountByEmail(String email);

    List<Account> findAccountsByRole(Role role);

    @Query("select count(a) from Account a where a.role = :role ")
    long countByRole(@Param("role") Role role);

    Optional<Account> findByEmail(String email);

}
