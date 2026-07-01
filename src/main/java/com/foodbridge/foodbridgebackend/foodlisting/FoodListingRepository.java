package com.foodbridge.foodbridgebackend.foodlisting;

import com.foodbridge.foodbridgebackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodListingRepository extends JpaRepository<FoodListing, Long> {

    List<FoodListing> findByDonor(User donor);

    List<FoodListing> findByStatus(ListingStatus status);
    
    List<FoodListing> findByAcceptedByNgo(User ngo);
    
}