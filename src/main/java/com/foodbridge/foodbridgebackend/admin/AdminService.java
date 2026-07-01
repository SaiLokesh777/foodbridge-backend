package com.foodbridge.foodbridgebackend.admin;

import com.foodbridge.foodbridgebackend.admin.dto.ReportSummaryDto;
import com.foodbridge.foodbridgebackend.foodlisting.FoodListingRepository;
import com.foodbridge.foodbridgebackend.foodlisting.ListingStatus;
import com.foodbridge.foodbridgebackend.user.Role;
import com.foodbridge.foodbridgebackend.user.User;
import com.foodbridge.foodbridgebackend.user.UserRepository;
import com.foodbridge.foodbridgebackend.user.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private com.foodbridge.foodbridgebackend.notification.EmailService emailService;
    
    @Autowired
    private FoodListingRepository foodListingRepository;

    public User approveNgo(Long ngoId) {
        User ngo = userRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        if (ngo.getRole() != Role.NGO) {
            throw new RuntimeException("This user is not an NGO");
        }

        ngo.setStatus(UserStatus.ACTIVE);
        User savedNgo = userRepository.save(ngo);

        emailService.sendEmail(
            savedNgo.getEmail(),
            "Food Bridge - Your account is approved!",
            "Hi " + savedNgo.getName() + ",\n\n"
            + "Good news! Your NGO account has been approved. You can now log in to Food Bridge "
            + "and start accepting available food listings.\n\n"
            + "Food Bridge Team"
        );

        return savedNgo;
    }
    
    public List<User> getAllDonors() {
        return userRepository.findByRole(Role.DONOR);
    }

    public List<User> getAllNgos() {
        return userRepository.findByRole(Role.NGO);
    }
    
    public List<User> getPendingNgos() {
        return userRepository.findByRoleAndStatus(Role.NGO, UserStatus.PENDING);
    }

    public User rejectNgo(Long ngoId) {
        User ngo = userRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        if (ngo.getRole() != Role.NGO) {
            throw new RuntimeException("This user is not an NGO");
        }

        ngo.setStatus(UserStatus.REJECTED);
        return userRepository.save(ngo);
    }

    public User suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.SUSPENDED);
        return userRepository.save(user);
    }

    public List<com.foodbridge.foodbridgebackend.foodlisting.FoodListing> getAllListings() {
        return foodListingRepository.findAll();
    }

    public ReportSummaryDto getReportSummary() {
        List<com.foodbridge.foodbridgebackend.foodlisting.FoodListing> allListings = foodListingRepository.findAll();

        long delivered = allListings.stream()
                .filter(l -> l.getStatus() == ListingStatus.DELIVERED)
                .count();

        long totalMealsSaved = allListings.stream()
                .filter(l -> l.getStatus() == ListingStatus.DELIVERED)
                .mapToInt(l -> l.getPeopleFedCount() != null ? l.getPeopleFedCount() : 0)
                .sum();

        long activeDonors = userRepository.countByRoleAndStatus(Role.DONOR, UserStatus.ACTIVE);
        long activeNgos = userRepository.countByRoleAndStatus(Role.NGO, UserStatus.ACTIVE);

        double deliveryRate = allListings.isEmpty() ? 0.0 :
                (delivered * 100.0) / allListings.size();

        return new ReportSummaryDto(totalMealsSaved, activeDonors, activeNgos, deliveryRate);
    }
}