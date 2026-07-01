package com.foodbridge.foodbridgebackend.foodlisting;

import com.foodbridge.foodbridgebackend.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_listings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @Column(name = "food_name", nullable = false, length = 150)
    private String foodName;

    @Column(nullable = false, length = 50)
    private String quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type", nullable = false)
    private FoodType foodType;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "prepared_time", nullable = false)
    private LocalDateTime preparedTime;

    @Column(name = "available_until", nullable = false)
    private LocalDateTime availableUntil;

    @Column(name = "pickup_address", nullable = false, length = 255)
    private String pickupAddress;

    @Column(length = 255)
    private String notes;

    @Column(name = "safety_confirmed", nullable = false)
    private Boolean safetyConfirmed = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingStatus status = ListingStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "accepted_by_ngo_id")
    private User acceptedByNgo;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "collected_photo_url", length = 500)
    private String collectedPhotoUrl;

    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    @Column(name = "delivered_photo_url", length = 500)
    private String deliveredPhotoUrl;

    @Column(name = "beneficiary_type")
    private String beneficiaryType;

    @Column(name = "people_fed_count")
    private Integer peopleFedCount;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}