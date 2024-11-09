package com.example.demo.repository;

import com.example.demo.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    Reward findRewardById(Long id);
    Reward findRewardByName(String name);
    List<Reward> findRewardsByIsDeletedFalse();
}
