package com.foodbridge.foodbridgebackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NgoProfileRepository extends JpaRepository<NgoProfile, Long> {
    Optional<NgoProfile> findByUser(User user);
}