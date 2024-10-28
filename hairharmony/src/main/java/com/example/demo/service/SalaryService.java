package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Stylist;
import com.example.demo.entity.Transactions;
import com.example.demo.entity.enums.Role;
import com.example.demo.entity.enums.TransactionsEnums;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SalaryService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Scheduled(cron = "0 47 8 28 * ?") // This cron expression represents the first day of every month at 00:00
    @Transactional
    public void createSalary() {
        Account manager = accountRepository.findAccountsByRole(Role.MANAGER).get(0);

        // Find all stylists
        List<Account> stylists = accountRepository.findAccountsByRole(Role.STYLIST);

        float totalSalary = 0.0f;

        for (Account stylist : stylists) {
            float stylistSalary = calculateMonthlySalary(stylist); // Calculate salary for the stylist

            // Create a transaction for each stylist
            Transactions transaction = new Transactions();
            transaction.setFrom(manager); // Manager sends the payment
            transaction.setTo(stylist); // Stylist receives the payment
            transaction.setAmount(stylistSalary);
            transaction.setStatus(TransactionsEnums.SUCCESS);
            transaction.setDescription("Trả lương tháng");
            transaction.setCreateAt(new Date());

            // Save the transaction
            transactionRepository.save(transaction);
            stylist.setBalance(stylist.getBalance() + stylistSalary);
            accountRepository.save(stylist);
            totalSalary += stylistSalary;
        }
        // Update manager and stylist balances
        manager.setBalance(manager.getBalance() - totalSalary);
        accountRepository.save(manager);
    }

    public float calculateMonthlySalary(Account stylistAccount) {
        float totalSalary = 0.0f;

        // Iterate over each stylist in the list and calculate their individual salaries
        for (Stylist stylist : stylistAccount.getStylists()) {
            // Calculate base salary based on stylist level
            float baseSalary = calculateBaseSalaryByLevel(stylist.getLevel());

            // Calculate commission based on positive feedback (rating > 4)
            float commission = calculateCommissionByFeedback(stylistAccount);

            // Add this stylist's salary to the total salary
            totalSalary += baseSalary + commission;
        }

        return totalSalary;
    }


    private float calculateBaseSalaryByLevel(int level) {
            switch (level) {
                case 1:
                    return 12000000.0f;
                case 2:
                    return 15000000.0f;
                case 3:
                    return 18000000.0f;
                case 4:
                    return 21000000.0f;
                case 5:
                    return 24000000.0f;
                default:
                    return 10000000.0f; // Default for unclassified levels
            }
        }

    private float calculateCommissionByFeedback(Account stylist) {
        // Retrieve feedback with ratings >=4 stars for this stylist

        List<Feedback> positiveFeedbacks =  feedbackRepository.findByStylistAndRatingGreaterThanEqualAndIsDeletedFalse(stylist, 4);
        float commissionPerFeedback = 30000.0f;
        return positiveFeedbacks.size() * commissionPerFeedback;
    }

}
