package com.foodbridge.foodbridgebackend.auth.dto;

import lombok.Data;

@Data
public class RegisterDonorRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String businessName;
    private String businessType; // RESTAURANT, HOTEL, MARRIAGE_HALL, HOSTEL
    private String fssaiLicense;
}