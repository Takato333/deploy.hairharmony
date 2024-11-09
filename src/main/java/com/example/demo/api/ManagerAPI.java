package com.example.demo.api;

import com.example.demo.entity.Manager;
import com.example.demo.model.ManagerRequest;
import com.example.demo.model.ManagerResponse;
import com.example.demo.service.ManagerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin("*")
@SecurityRequirement(name = "api")

public class ManagerAPI {

    @Autowired
    ManagerService managerService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity createManager(@Valid @RequestBody ManagerRequest managerRequest) {
        ManagerResponse newManager = managerService.createManager(managerRequest);
        return ResponseEntity.ok(newManager);
    }

    @GetMapping
    public ResponseEntity getAllManagers() {
        List<Manager> allManagers = managerService.getAllManagers();
        return ResponseEntity.ok(allManagers);
    }

    @PutMapping("{managerId}")
    public ResponseEntity updateManager(@PathVariable Long managerId, @Valid @RequestBody ManagerRequest managerRequest) {
        Manager updatedManager = managerService.updateManager(managerId, managerRequest);
        return ResponseEntity.ok(updatedManager);
    }

    // Find manager by ID
    @GetMapping("{managerId}")
    public ResponseEntity getManagerById(@PathVariable long managerId) {
        Manager manager = managerService.getManagerById(managerId);
        return ResponseEntity.ok(manager);
    }

    @DeleteMapping("{managerId}")
    public ResponseEntity deleteManager(@PathVariable long managerId) {
        Manager deletedManager = managerService.deleteManager(managerId);
        return ResponseEntity.ok(deletedManager);
    }
}
