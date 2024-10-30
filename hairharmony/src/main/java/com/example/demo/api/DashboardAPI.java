package com.example.demo.api;

import com.example.demo.service.DashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class DashboardAPI {

    @Autowired
    DashboardService dashboardService;

    @GetMapping("/admin")
    public ResponseEntity getDashboardAdmin() {
        Map<String, Object> stats = dashboardService.getDashboardAdminStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/staff")
    public ResponseEntity getDashboardStaff() {
        Map<String, Object> stats = dashboardService.getDashboardStaffStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/feedback")
    public ResponseEntity getDashboardFeedback() {
        Map<String, Object> stats = dashboardService.getDashboardFeedBackStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/reward")
    public ResponseEntity getDashboardReward() {
        Map<String, Object> stats = dashboardService.getDashboardRewardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/top3Service")
    public ResponseEntity getDashboardTop3Service() {
        Map<String, Object> stats = dashboardService.getDashboardServiceStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity getDashboardMonthlyRevenue() {
        Map<String, Object> revenueMonthly = dashboardService.getDashboardMonthlyRevenue();
        return ResponseEntity.ok(revenueMonthly);
    }

    @GetMapping("/{stylistId}/kpi")
    public ResponseEntity<Map<String, Object>> getKPIDashboard(@PathVariable Long stylistId) {
        // Lấy thời gian bắt đầu của tháng hiện tại
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        Map<String, Object> dashboard = dashboardService.getStylistDashboard(stylistId, startOfMonth);
        return ResponseEntity.ok(dashboard);
    }


}
