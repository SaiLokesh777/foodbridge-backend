package com.foodbridge.foodbridgebackend.auth.dto;

import lombok.Data;

@Data
public class RegisterNgoRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String ngoName;
    private String registrationNumber;
    private String certificateUrl;
}