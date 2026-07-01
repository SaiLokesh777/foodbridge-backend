package com.foodbridge.foodbridgebackend.admin;

import com.foodbridge.foodbridgebackend.admin.dto.ReportSummaryDto;
import com.foodbridge.foodbridgebackend.foodlisting.FoodListing;
import com.foodbridge.foodbridgebackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/ngo/pending")
    public ResponseEntity<?> getPendingNgos() {
        List<User> pendingNgos = adminService.getPendingNgos();
        return ResponseEntity.ok(pendingNgos);
    }

    @PatchMapping("/ngo/{id}/approve")
    public ResponseEntity<?> approveNgo(@PathVariable Long id) {
        User approved = adminService.approveNgo(id);
        return ResponseEntity.ok(approved);
    }

    @PatchMapping("/ngo/{id}/reject")
    public ResponseEntity<?> rejectNgo(@PathVariable Long id) {
        User rejected = adminService.rejectNgo(id);
        return ResponseEntity.ok(rejected);
    }

    @GetMapping("/food-listings")
    public ResponseEntity<?> getAllListings() {
        List<FoodListing> listings = adminService.getAllListings();
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/reports/summary")
    public ResponseEntity<?> getReportSummary() {
        ReportSummaryDto summary = adminService.getReportSummary();
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/donors")
    public ResponseEntity<?> getAllDonors() {
        List<User> donors = adminService.getAllDonors();
        return ResponseEntity.ok(donors);
    }

    @GetMapping("/ngos")
    public ResponseEntity<?> getAllNgos() {
        List<User> ngos = adminService.getAllNgos();
        return ResponseEntity.ok(ngos);
    }

    @PatchMapping("/users/{id}/suspend")
    public ResponseEntity<?> suspendUser(@PathVariable Long id) {
        User suspended = adminService.suspendUser(id);
        return ResponseEntity.ok(suspended);
    }
}