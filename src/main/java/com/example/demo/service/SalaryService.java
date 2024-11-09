package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Stylist;
import com.example.demo.entity.Transactions;
import com.example.demo.entity.enums.Role;
import com.example.demo.entity.enums.TransactionsEnums;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.TransactionsResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.StylistRepository;
import com.example.demo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class SalaryService {

    private static final float BASE_SALARY = 7000000.0f;
    private static final float SALARY_INCREMENT_PER_LEVEL = 3000000.0f;
    private static final float DEFAULT_SALARY = 5000000.0f;
    private static final float COMMISSION_PER_FEEDBACK = 30000.0f;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    StylistRepository stylistRepository;

    @Scheduled(cron = "0 0 0 28 * ?") // This cron expression represents the first day of every month at 00:00
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

            // Calculate commission based on positive feedback (rating > 4)
            float commission = calculateCommissionByFeedback(stylist.getId());

            // Calculate base salary based on stylist level
            float baseSalary = calculateBaseSalaryByLevel(stylist.getLevel());

            // Add this stylist's salary to the total salary
            totalSalary += baseSalary + commission;
        }

        return totalSalary;
    }


    private float calculateBaseSalaryByLevel(int level) {
        if (level < 1 || level > 5) {
            return DEFAULT_SALARY; // Return default if level is unclassified
        }
        return BASE_SALARY + (SALARY_INCREMENT_PER_LEVEL * (level - 1)); // tại BASE_SALARY = level 1
    }

    private float calculateCommissionByFeedback(long stylistId) {
        // Retrieve feedback with ratings >=4 stars for this stylistt
        Stylist findStylist = stylistRepository.findStylistByIdAndIsDeletedFalse(stylistId);
        if(findStylist == null) {
            throw new EntityNotFoundException("Không có thợ!");
        }
        Account stylistAccount = findStylist.getAccount();
        List<Feedback> positiveFeedbacks =  feedbackRepository.findByStylistAndRatingGreaterThanEqualAndIsDeletedFalse(stylistAccount, 4);
        return positiveFeedbacks.size() * COMMISSION_PER_FEEDBACK;
    }

    public TransactionsResponse getAllTransactions(int page, int size) {
        Page transactionsPage = transactionRepository.findAll(PageRequest.of(page, size));
        TransactionsResponse transactionsResponse = new TransactionsResponse();
        transactionsResponse.setTotalPages(transactionsPage.getTotalPages());
        transactionsResponse.setContent(transactionsPage.getContent());
        transactionsResponse.setPageNumber(transactionsPage.getNumber());
        transactionsResponse.setTotalElements(transactionsPage.getTotalElements());
        return transactionsResponse;
    }




}
