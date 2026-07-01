package com.foodbridge.foodbridgebackend.admin.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ReportSummaryDto {
    private long totalMealsSaved;
    private long activeDonors;
    private long activeNgos;
    private double deliveryRate;
}