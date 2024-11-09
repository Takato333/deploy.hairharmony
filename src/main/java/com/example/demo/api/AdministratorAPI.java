package com.example.demo.api;

import com.example.demo.entity.Administrator;
import com.example.demo.model.AdministratorRequest;
import com.example.demo.model.AdministratorResponse;
import com.example.demo.service.AdministratorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrator")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AdministratorAPI {

    @Autowired
    AdministratorService administratorService;

    @PostMapping
    public ResponseEntity createAdministrator(@Valid @RequestBody AdministratorRequest administratorRequest) {
        AdministratorResponse newAdministrator = administratorService.createAdministrator(administratorRequest);
        return ResponseEntity.ok(newAdministrator);
    }


    @GetMapping
    public ResponseEntity getAllAdministrators() {
        List<Administrator> allAdministrators = administratorService.getAllAdministrators();
        return ResponseEntity.ok(allAdministrators);
    }


    @PutMapping("{administratorId}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity updateAdministrator(@PathVariable Long administratorId, @Valid @RequestBody AdministratorRequest administratorRequest) {
        Administrator updatedAdministrator = administratorService.updateAdministrator(administratorId, administratorRequest);
        return ResponseEntity.ok(updatedAdministrator);
    }

    // Find administrator by ID
    @GetMapping("{administratorId}")
    public ResponseEntity getAdministratorById(@PathVariable long administratorId) {
        Administrator administrator = administratorService.getAdministratorById(administratorId);
        return ResponseEntity.ok(administrator);
    }


    @DeleteMapping("{administratorId}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity deleteAdministrator(@PathVariable long administratorId) {
        Administrator deletedAdministrator = administratorService.deleteAdministrator(administratorId);
        return ResponseEntity.ok(deletedAdministrator);
    }

}
