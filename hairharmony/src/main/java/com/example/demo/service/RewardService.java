package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Reward;
import com.example.demo.entity.RewardClaimed;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RewardClaimedRepository;
import com.example.demo.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardService {

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RewardClaimedRepository rewardClaimedRepository;

    //Create
    public Reward createReward(RewardRequest rewardRequest) {
        try{
            Reward reward = new Reward();
            reward.setName(rewardRequest.getName());
            reward.setDescription(rewardRequest.getDescription());
            reward.setImage(rewardRequest.getImage());
            reward.setLoyaltyPointRequire(rewardRequest.getLoyaltyPointRequire());
            return rewardRepository.save(reward);
        }catch (Exception e){
            throw new DuplicateEntity("Duplicate reward! Fail to create!");
        }
    }

    //Read
    public List<Reward> getAllRewards() {
        List<Reward> rewards = rewardRepository.findRewardsByIsDeletedFalse();
        return rewards;
    }

    public Reward getRewardById(Long id) {
        Reward reward = rewardRepository.findRewardById(id);
        if(reward == null){
            throw new EntityNotFoundException("Reward not found!");
        }
        return reward;
    }

    //Update
    public Reward updateRewardById(long id, Reward reward) {
        Reward oldReward = getRewardById(id);
        oldReward.setName(reward.getName());
        oldReward.setDescription(reward.getDescription());
        oldReward.setImage(reward.getImage());
        oldReward.setLoyaltyPointRequire(reward.getLoyaltyPointRequire());
        return rewardRepository.save(oldReward);
    }

    //Delete
    public Reward deleteRewardById(long id) {
        Reward oldReward = rewardRepository.findRewardById(id);
        oldReward.setDeleted(true);
        return rewardRepository.save(oldReward);
    }

    public String claimReward(Long customerId, Long rewardId) {
        // Tìm Customer theo customerId
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng!"));

        // Tìm Reward theo rewardId
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quà!"));

        // Kiểm tra loyaltyPoint của customer
        if (customer.getLoyaltyPoint() >= reward.getLoyaltyPointRequire()) {
            // Trừ loyaltyPoint tương ứng với số điểm yêu cầu
            customer.setLoyaltyPoint(customer.getLoyaltyPoint() - reward.getLoyaltyPointRequire());
            customerRepository.save(customer);

            // Tạo một RewardClaimed mới để lưu lại giao dịch
            RewardClaimed rewardClaimed = new RewardClaimed();
            rewardClaimed.setCustomer(customer);
            rewardClaimed.setReward(reward);

            // Lưu thông tin RewardClaimed
            rewardClaimedRepository.save(rewardClaimed);

            return "Nhận quà thành công!";
        } else {
            return "Quý khách chưa tích đủ số điểm để nhận phần quà này!";
        }
    }

    //show cho customer thấy những reward mình đã đặt
    public List<Reward> getRewardsByCustomerId(Long customerId) {
        List<RewardClaimed> rewardClaimedList = rewardClaimedRepository.findAllByCustomerId(customerId);

        // Chuyển đổi danh sách RewardClaimed thành danh sách Reward để giữ tất cả các phần thưởng
        return rewardClaimedList.stream()
                .map(RewardClaimed::getReward)
                .collect(Collectors.toList());
    }

    //show tất cả reward mà tất cả customer đặt
    public RewardClaimedResponse getAllRewardClaimed(int page, int size) {
        Page rewardClaimedPage = rewardClaimedRepository.findAll(PageRequest.of(page, size));
        RewardClaimedResponse rewardClaimedResponse = new RewardClaimedResponse();
        rewardClaimedResponse.setTotalPages(rewardClaimedPage.getTotalPages());
        rewardClaimedResponse.setContent(rewardClaimedPage.getContent());
        rewardClaimedResponse.setPageNumber(rewardClaimedPage.getNumber());
        rewardClaimedResponse.setTotalElements(rewardClaimedPage.getTotalElements());
        return rewardClaimedResponse;
    }


}
