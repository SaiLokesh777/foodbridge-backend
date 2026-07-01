package com.foodbridge.foodbridgebackend.foodlisting;

import com.foodbridge.foodbridgebackend.foodlisting.dto.PostFoodRequest;
import com.foodbridge.foodbridgebackend.foodlisting.dto.CollectFoodRequest;
import com.foodbridge.foodbridgebackend.foodlisting.dto.DeliverFoodRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FoodListingController {

    @Autowired
    private FoodListingService foodListingService;

    // ----- DONOR ENDPOINT -----

    @PostMapping("/api/donor/food-listings")
    public ResponseEntity<?> postFood(@RequestBody PostFoodRequest request, HttpServletRequest httpRequest) {
        Long donorId = (Long) httpRequest.getAttribute("userId");
        FoodListing savedListing = foodListingService.postFood(donorId, request);
        return ResponseEntity.ok(savedListing);
    }

    // ----- NGO ENDPOINTS -----

    @GetMapping("/api/ngo/food-listings/available")
    public ResponseEntity<?> getAvailableListings() {
        List<FoodListing> listings = foodListingService.getAvailableListings();
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/api/ngo/food-listings/my-pickups")
    public ResponseEntity<?> getMyPickups(HttpServletRequest httpRequest) {
        Long ngoId = (Long) httpRequest.getAttribute("userId");
        List<FoodListing> listings = foodListingService.getMyPickups(ngoId);
        return ResponseEntity.ok(listings);
    }
    
    @PatchMapping("/api/ngo/food-listings/{id}/cancel")
    public ResponseEntity<?> cancelAcceptance(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long ngoId = (Long) httpRequest.getAttribute("userId");
        FoodListing updated = foodListingService.cancelAcceptance(id, ngoId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/api/ngo/food-listings/{id}/accept")
    public ResponseEntity<?> acceptFood(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long ngoId = (Long) httpRequest.getAttribute("userId");
        FoodListing updated = foodListingService.acceptFood(id, ngoId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/api/ngo/food-listings/{id}/collect")
    public ResponseEntity<?> collectFood(@PathVariable Long id, @RequestBody CollectFoodRequest request, HttpServletRequest httpRequest) {
        Long ngoId = (Long) httpRequest.getAttribute("userId");
        FoodListing updated = foodListingService.collectFood(id, ngoId, request);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/api/donor/food-listings")
    public ResponseEntity<?> getMyPosts(HttpServletRequest httpRequest) {
        Long donorId = (Long) httpRequest.getAttribute("userId");
        List<FoodListing> listings = foodListingService.getMyPosts(donorId);
        return ResponseEntity.ok(listings);
    }
    

    @PatchMapping("/api/ngo/food-listings/{id}/deliver")
    public ResponseEntity<?> deliverFood(@PathVariable Long id, @RequestBody DeliverFoodRequest request, HttpServletRequest httpRequest) {
        Long ngoId = (Long) httpRequest.getAttribute("userId");
        FoodListing updated = foodListingService.deliverFood(id, ngoId, request);
        return ResponseEntity.ok(updated);
    }
}