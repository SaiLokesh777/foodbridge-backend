package com.foodbridge.foodbridgebackend.foodlisting;
import org.springframework.scheduling.annotation.Scheduled;

import com.foodbridge.foodbridgebackend.foodlisting.dto.PostFoodRequest;
import com.foodbridge.foodbridgebackend.foodlisting.dto.CollectFoodRequest;
import com.foodbridge.foodbridgebackend.foodlisting.dto.DeliverFoodRequest;
import com.foodbridge.foodbridgebackend.user.User;
import com.foodbridge.foodbridgebackend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.foodbridge.foodbridgebackend.user.UserStatus;
import com.foodbridge.foodbridgebackend.notification.NotificationService;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodListingService {

    @Autowired
    private FoodListingRepository foodListingRepository;
    @Autowired
    private NotificationService notificationService;
    
    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void expireOldListings() {
        List<FoodListing> availableListings = foodListingRepository.findByStatus(ListingStatus.AVAILABLE);
        LocalDateTime now = LocalDateTime.now();

        for (FoodListing listing : availableListings) {
            if (listing.getAvailableUntil().isBefore(now)) {
                listing.setStatus(ListingStatus.EXPIRED);
                foodListingRepository.save(listing);
            }
        }
    }
    
    @Autowired
    private UserRepository userRepository;

    public FoodListing postFood(Long donorId, PostFoodRequest request) {

        if (request.getSafetyConfirmed() == null || !request.getSafetyConfirmed()) {
            throw new RuntimeException("You must confirm the food safety checkbox before posting");
        }

        User donor = userRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        FoodListing listing = new FoodListing();
        listing.setDonor(donor);
        listing.setFoodName(request.getFoodName());
        listing.setQuantity(request.getQuantity());
        listing.setFoodType(FoodType.valueOf(request.getFoodType()));
        listing.setPhotoUrl(request.getPhotoUrl());
        listing.setPreparedTime(request.getPreparedTime());
        listing.setAvailableUntil(request.getAvailableUntil());
        listing.setPickupAddress(request.getPickupAddress());
        listing.setNotes(request.getNotes());
        listing.setSafetyConfirmed(true);
        listing.setStatus(ListingStatus.AVAILABLE);

        FoodListing savedListing = foodListingRepository.save(listing);

        notificationService.notifyNgosOfNewListing(savedListing);

        return savedListing;
    }

    public List<FoodListing> getAvailableListings() {
        return foodListingRepository.findByStatus(ListingStatus.AVAILABLE);
    }
    
    public List<FoodListing> getMyPosts(Long donorId) {
        User donor = userRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        return foodListingRepository.findByDonor(donor);
    }
    

    public List<FoodListing> getMyPickups(Long ngoId) {
        User ngo = userRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found"));
        return foodListingRepository.findByAcceptedByNgo(ngo);
    }

    public FoodListing acceptFood(Long listingId, Long ngoId) {
        FoodListing listing = foodListingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if (listing.getStatus() != ListingStatus.AVAILABLE) {
            throw new RuntimeException("This listing is no longer available");
        }

        User ngo = userRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        if (ngo.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("Your NGO account is not yet approved by admin");
        }

        listing.setStatus(ListingStatus.ACCEPTED);
        listing.setAcceptedByNgo(ngo);
        listing.setAcceptedAt(LocalDateTime.now());

        return foodListingRepository.save(listing);
    }

    public FoodListing collectFood(Long listingId, Long ngoId, CollectFoodRequest request) {
        FoodListing listing = foodListingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if (listing.getStatus() != ListingStatus.ACCEPTED) {
            throw new RuntimeException("This listing must be accepted before it can be collected");
        }

        if (!listing.getAcceptedByNgo().getId().equals(ngoId)) {
            throw new RuntimeException("You did not accept this listing");
        }

        listing.setStatus(ListingStatus.COLLECTED);
        listing.setCollectedPhotoUrl(request.getCollectedPhotoUrl());
        listing.setCollectedAt(LocalDateTime.now());

        return foodListingRepository.save(listing);
    }
    
    public FoodListing cancelAcceptance(Long listingId, Long ngoId) {
        FoodListing listing = foodListingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if (listing.getStatus() != ListingStatus.ACCEPTED) {
            throw new RuntimeException("Only an accepted listing can be cancelled");
        }

        if (!listing.getAcceptedByNgo().getId().equals(ngoId)) {
            throw new RuntimeException("You did not accept this listing");
        }

        listing.setStatus(ListingStatus.AVAILABLE);
        listing.setAcceptedByNgo(null);
        listing.setAcceptedAt(null);

        FoodListing savedListing = foodListingRepository.save(listing);

        notificationService.notifyNgosOfNewListing(savedListing);

        return savedListing;
    }

    public FoodListing deliverFood(Long listingId, Long ngoId, DeliverFoodRequest request) {
        FoodListing listing = foodListingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if (listing.getStatus() != ListingStatus.COLLECTED) {
            throw new RuntimeException("This listing must be collected before it can be marked as delivered");
        }

        if (!listing.getAcceptedByNgo().getId().equals(ngoId)) {
            throw new RuntimeException("You did not accept this listing");
        }

        listing.setStatus(ListingStatus.DELIVERED);
        listing.setDeliveredPhotoUrl(request.getDeliveredPhotoUrl());
        listing.setBeneficiaryType(request.getBeneficiaryType());
        listing.setPeopleFedCount(request.getPeopleFedCount());
        listing.setDeliveredAt(LocalDateTime.now());

        return foodListingRepository.save(listing);
    }
}