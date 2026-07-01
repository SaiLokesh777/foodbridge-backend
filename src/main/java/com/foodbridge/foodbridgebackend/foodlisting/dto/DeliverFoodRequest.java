package com.foodbridge.foodbridgebackend.foodlisting.dto;

import lombok.Data;

@Data
public class DeliverFoodRequest {
    private String deliveredPhotoUrl;
    private String beneficiaryType;
    private Integer peopleFedCount;
}