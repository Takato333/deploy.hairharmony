package com.example.demo.api;

import com.example.demo.entity.Staff;
import com.example.demo.entity.Stylist;
import com.example.demo.model.ServiceResponse;
import com.example.demo.model.StaffRequest;
import com.example.demo.model.StaffResponse;
import com.example.demo.service.StaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class StaffAPI {
    @Autowired
    StaffService staffService;

    @PostMapping
    public ResponseEntity createStaff(@Valid @RequestBody StaffRequest staffRequest) {
        StaffResponse  newStaff = staffService.createStaff(staffRequest);
        return ResponseEntity.ok(newStaff);
    }

    @GetMapping
    public ResponseEntity getAllStaff() {
        List<Staff> allStaff = staffService.getAllStaff();
        return ResponseEntity.ok(allStaff);
    }



    @PutMapping("{staffId}")
    public ResponseEntity updateStaff(@PathVariable Long staffId, @Valid @RequestBody StaffRequest staffRequest) {
        Staff updatedStaff =staffService.updateStaff(staffId, staffRequest);
        return ResponseEntity.ok(updatedStaff);
    }

    //tìm staff bằng id
    @GetMapping("{staffId}")
    public ResponseEntity getStaffById(@PathVariable long staffId) {
        Staff staff = staffService.getStaffById(staffId);
        return ResponseEntity.ok(staff);
    }

    @DeleteMapping("{staffId}")
    public ResponseEntity deleteStaff(@PathVariable long staffId) {
        Staff deletedStaff = staffService.deleteStaff(staffId);
        return ResponseEntity.ok(deletedStaff);
    }
}
