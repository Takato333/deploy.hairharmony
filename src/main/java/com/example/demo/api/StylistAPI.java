package com.example.demo.api;

import com.example.demo.entity.Stylist;
import com.example.demo.model.StylistRequest;
import com.example.demo.model.StylistResponse;
import com.example.demo.service.StylistService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stylist")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class StylistAPI
{
    @Autowired
    StylistService stylistService;


    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity createStylist(@Valid @RequestBody StylistRequest stylistRequest) {
        StylistResponse newStylist = stylistService.createStylist(stylistRequest);
        return ResponseEntity.ok(newStylist);
    }

    @GetMapping
    public ResponseEntity getAllStylists() {
        List<Stylist> stylists = stylistService.getAllStylists();
        return ResponseEntity.ok(stylists);
    }


    //tìm stylist bằng id
    @GetMapping("{stylistId}")
    public ResponseEntity getStylistById(@PathVariable long stylistId) {
        Stylist stylist = stylistService.getStylistById(stylistId);
        return ResponseEntity.ok(stylist);
    }

    //tìm những stylist bằng level
    @GetMapping("/level:{stylistLevel}")
    public ResponseEntity getStylistsByLevel(@PathVariable int stylistLevel) {
        List<Stylist> stylists = stylistService.getStylistsByLevel(stylistLevel);
        return ResponseEntity.ok(stylists);
    }


    @PutMapping("{stylistId}")
    public ResponseEntity updateStylist(@PathVariable long stylistId, @Valid @RequestBody StylistRequest stylistRequest) {
        Stylist updatedStylist = stylistService.updateStylist(stylistId, stylistRequest);
        return ResponseEntity.ok(updatedStylist);
    }


    @DeleteMapping("{stylistId}")
    public ResponseEntity deleteStylist(@PathVariable long stylistId) {
        Stylist deletedStylist = stylistService.deleteStylist(stylistId);
        return ResponseEntity.ok(deletedStylist);
    }



}
