package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    public Map<String, Object> getDashboardAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        //số lượng customer
        long customerCount = accountRepository.countByRole(Role.CUSTOMER);
        stats.put("customerCount",customerCount);

        //số lượng stylist
        long stylistCount = accountRepository.countByRole(Role.STYLIST);
        stats.put("stylistCount",stylistCount);

        //số lương staff
        long staffCount = accountRepository.countByRole(Role.STAFF);
        stats.put("staffCount",staffCount);

        return stats;
    }

    public Map<String, Object> getDashboardStaffStats() {
        Map<String, Object> stats = new HashMap<>();
        //số lương service
        long totalProducts = serviceRepository.count();
        stats.put("totalProducts",totalProducts);

        //số lượng appointment
        long totalAppointments = appointmentRepository.count();
        stats.put("totalAppointments",totalAppointments);

        return stats;
    }

    public Map<String, Object> getDashboardServiceStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Object[]> topServices = serviceRepository.findTop3BestChoiceServices();
        List<Map<String,Object>> topServiceList = new ArrayList<>();

        for(Object[] service : topServices) {
            Map<String,Object> productInfo = new HashMap<>();
            productInfo.put("productName",service[0]);
            productInfo.put("totalBooked",service[1]);
            topServiceList.add(productInfo);
        }

        stats.put("topServices",topServiceList);
        return stats;
    }

    public Map<String, Object> getDashboardMonthlyRevenue() {
        Map<String, Object> revenueData = new HashMap<>();
        Account account = authenticationService.getCurrentAccount();
        if (account == null) {
            throw new EntityNotFoundException("Bạn cần đăng nhập trước");
        }
        List<Object[]> monthlyRevenue = transactionRepository.calculateMonthlyRevenue(account.getId());
        List<Map<String,Object>> monthlyRevenueList = new ArrayList<>();

        for (Object[] result : monthlyRevenue) {
            Map<String,Object> monthData = new HashMap<>();
            monthData.put("year",result[0]);
            monthData.put("month",result[1]);
            monthData.put("totalRevenue",result[2]);
            monthlyRevenueList.add(monthData);
        }
        revenueData.put("monthlyRevenue",monthlyRevenueList);
        return revenueData;
    }



}
