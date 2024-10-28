package com.example.demo.api;

import com.example.demo.entity.Reward;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.entity.Stylist;
import com.example.demo.model.RewardRequest;
import com.example.demo.model.ServiceRequest;
import com.example.demo.model.ServiceResponse;
import com.example.demo.service.RewardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reward")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class RewardAPI {

    @Autowired
    private RewardService rewardService;

    @PostMapping("/claim")
    public ResponseEntity<String> claimReward(@RequestParam Long customerId, @RequestParam Long rewardId) {
        String result = rewardService.claimReward(customerId, rewardId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity createReward (@Valid @RequestBody RewardRequest rewardRequest) {
        Reward newReward = rewardService.createReward(rewardRequest);
        return ResponseEntity.ok(newReward);
    }

    @GetMapping
    public ResponseEntity getAllRewards() {
        List<Reward> rewards = rewardService.getAllRewards();
        return ResponseEntity.ok(rewards);
    }

    @PutMapping("{rewardId}")
    public ResponseEntity updateReward(@PathVariable long rewardId, @Valid @RequestBody Reward reward) {
         Reward updatedReward = rewardService.updateRewardById(rewardId, reward);
        return ResponseEntity.ok(updatedReward);
    }

    @DeleteMapping("{rewardId}")
    public ResponseEntity deleteReward(@PathVariable long rewardId) {
       Reward reward = rewardService.deleteRewardById(rewardId);
       return ResponseEntity.ok(reward);
    }


}
