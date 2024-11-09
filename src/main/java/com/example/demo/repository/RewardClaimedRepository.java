package com.example.demo.repository;

import com.example.demo.entity.Reward;
import com.example.demo.entity.RewardClaimed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RewardClaimedRepository extends JpaRepository<RewardClaimed, Long> {
    @Query("SELECT rc FROM RewardClaimed rc WHERE rc.customer.id = :customerId AND rc.isDeleted = false")
    List<RewardClaimed> findAllByCustomerId(@Param("customerId") Long customerId);

    Page<RewardClaimed> findAll(Pageable pageable);

}
