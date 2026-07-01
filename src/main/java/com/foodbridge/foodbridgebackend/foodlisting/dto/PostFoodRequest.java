package com.foodbridge.foodbridgebackend.foodlisting.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostFoodRequest {
    private String foodName;
    private String quantity;
    private String foodType; // VEG or NON_VEG
    private String photoUrl;
    private LocalDateTime preparedTime;
    private LocalDateTime availableUntil;
    private String pickupAddress;
    private String notes;
    private Boolean safetyConfirmed;
}