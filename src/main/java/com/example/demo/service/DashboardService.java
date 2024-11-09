package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    FeedbackRepository feedbackRepository;

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RewardClaimedRepository rewardClaimedRepository;

    @Autowired
    AppointmentDetailRepository appointmentDetailRepository;

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

    public Map<String, Object> getDashboardRewardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Tổng số phản hồi
        long totalFeedback = feedbackRepository.count();
        stats.put("totalFeedback", totalFeedback);

        // Đếm số lượng phản hồi theo từng mức đánh giá từ 5 đến 1 sao
        long feedback5Stars = feedbackRepository.countByRating(5);
        long feedback4Stars = feedbackRepository.countByRating(4);
        long feedback3Stars = feedbackRepository.countByRating(3);
        long feedback2Stars = feedbackRepository.countByRating(2);
        long feedback1Star = feedbackRepository.countByRating(1);

        // Thêm vào map kết quả
        stats.put("feedback5Stars", feedback5Stars);
        stats.put("feedback4Stars", feedback4Stars);
        stats.put("feedback3Stars", feedback3Stars);
        stats.put("feedback2Stars", feedback2Stars);
        stats.put("feedback1Star", feedback1Star);

        return stats;
    }

    public Map<String, Object> getDashboardFeedBackStats() {
        Map<String, Object> stats = new HashMap<>();

        // Tổng số phản hồi
        long totalFeedback = feedbackRepository.count();
        stats.put("totalFeedback", totalFeedback);

        // Đếm số lượng phản hồi theo từng mức đánh giá từ 5 đến 1 sao
        long feedback5Stars = feedbackRepository.countByRating(5);
        long feedback4Stars = feedbackRepository.countByRating(4);
        long feedback3Stars = feedbackRepository.countByRating(3);
        long feedback2Stars = feedbackRepository.countByRating(2);
        long feedback1Star = feedbackRepository.countByRating(1);

        // Thêm vào map kết quả
        stats.put("feedback5Stars", feedback5Stars);
        stats.put("feedback4Stars", feedback4Stars);
        stats.put("feedback3Stars", feedback3Stars);
        stats.put("feedback2Stars", feedback2Stars);
        stats.put("feedback1Star", feedback1Star);

        return stats;
    }

    public Map<String, Object> getStylistDashboard(Long stylistId, LocalDateTime startOfMonth) {
        Map<String, Object> dashboard = new HashMap<>();

        // Lấy số lượng cuộc hẹn đã đặt cho stylist theo stylistId trong tháng hiện tại
        int totalBookedAppointments = appointmentDetailRepository.countBookedAppointmentsByStylistId(stylistId, startOfMonth);

        // KPI appointment
        int kpiAppointment = 50;

        // Tính toán KPI đạt được
        boolean kpiMet = totalBookedAppointments >= kpiAppointment;

        dashboard.put("totalBookedAppointments", totalBookedAppointments);
        dashboard.put("kpiAppointment", kpiAppointment);
        dashboard.put("kpiMet", kpiMet);

        return dashboard;
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
