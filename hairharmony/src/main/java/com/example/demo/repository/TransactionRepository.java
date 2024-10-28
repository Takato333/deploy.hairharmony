package com.example.demo.repository;

import com.example.demo.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    @Query("select YEAR(t.createAt) as year, MONTH(t.createAt) as month, sum(t.amount)" +
            " from Transactions t " +
            "where t.status='SUCCESS' and t.to.id =:userId" +
            " GROUP BY YEAR(t.createAt), MONTH(t.createAt)" +
            "ORDER BY YEAR(t.createAt), MONTH(t.createAt) ")
    List<Object[]> calculateMonthlyRevenue(@Param("userId") long userId );
}
