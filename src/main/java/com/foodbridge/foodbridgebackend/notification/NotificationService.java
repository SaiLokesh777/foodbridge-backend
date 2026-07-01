package com.foodbridge.foodbridgebackend.notification;

import com.foodbridge.foodbridgebackend.foodlisting.FoodListing;
import com.foodbridge.foodbridgebackend.user.Role;
import com.foodbridge.foodbridgebackend.user.User;
import com.foodbridge.foodbridgebackend.user.UserRepository;
import com.foodbridge.foodbridgebackend.user.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void notifyNgosOfNewListing(FoodListing listing) {
        List<User> activeNgos = userRepository.findByRoleAndStatus(Role.NGO, UserStatus.ACTIVE);

        String subject = "New food available: " + listing.getFoodName();
        String body = "Hello,\n\n"
                + listing.getQuantity() + " of " + listing.getFoodName()
                + " is available for pickup.\n\n"
                + "Pickup address: " + listing.getPickupAddress() + "\n"
                + "Available until: " + listing.getAvailableUntil() + "\n\n"
                + "Log in to Food Bridge to accept this listing.\n\n"
                + "Thank you,\nFood Bridge Team";

        for (User ngo : activeNgos) {
            emailService.sendEmail(ngo.getEmail(), subject, body);
        }
    }
}