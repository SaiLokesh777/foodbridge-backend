package com.foodbridge.foodbridgebackend.auth;

import com.foodbridge.foodbridgebackend.auth.dto.LoginRequest;
import com.foodbridge.foodbridgebackend.auth.dto.RegisterDonorRequest;
import com.foodbridge.foodbridgebackend.auth.dto.RegisterNgoRequest;
import com.foodbridge.foodbridgebackend.notification.EmailService;
import com.foodbridge.foodbridgebackend.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonorProfileRepository donorProfileRepository;

    @Autowired
    private NgoProfileRepository ngoProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerDonor(RegisterDonorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(Role.DONOR);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        DonorProfile profile = new DonorProfile();
        profile.setUser(savedUser);
        profile.setBusinessName(request.getBusinessName());
        profile.setBusinessType(BusinessType.valueOf(request.getBusinessType()));
        profile.setFssaiLicense(request.getFssaiLicense());
        donorProfileRepository.save(profile);

        emailService.sendEmail(
            savedUser.getEmail(),
            "Welcome to Food Bridge!",
            "Hi " + savedUser.getName() + ",\n\n"
            + "Your donor account has been created successfully. "
            + "You can now log in and start posting surplus food.\n\n"
            + "Thank you for joining Food Bridge.\n\nFood Bridge Team"
        );

        return savedUser;
    }

    public User registerNgo(RegisterNgoRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(Role.NGO);
        user.setStatus(UserStatus.PENDING);

        User savedUser = userRepository.save(user);

        NgoProfile profile = new NgoProfile();
        profile.setUser(savedUser);
        profile.setNgoName(request.getNgoName());
        profile.setRegistrationNumber(request.getRegistrationNumber());
        profile.setCertificateUrl(request.getCertificateUrl());
        ngoProfileRepository.save(profile);

        emailService.sendEmail(
            savedUser.getEmail(),
            "Food Bridge - Application received",
            "Hi " + savedUser.getName() + ",\n\n"
            + "Thank you for registering " + request.getNgoName() + " on Food Bridge.\n\n"
            + "Your application is under review. You'll receive another email once approved.\n\n"
            + "Food Bridge Team"
        );

        emailService.sendEmail(
            "foodbridge.noreplay@gmail.com",
            "Food Bridge - New NGO registration pending approval",
            "Hi Admin,\n\n"
            + "A new NGO has registered and is waiting for your approval:\n\n"
            + "NGO Name: " + request.getNgoName() + "\n"
            + "Contact Person: " + request.getName() + "\n"
            + "Email: " + request.getEmail() + "\n"
            + "Registration Number: " + request.getRegistrationNumber() + "\n\n"
            + "Approve or reject here:\n"
            + "https://foodbridge-frontend-lilac.vercel.app/admin\n\n"
            + "Food Bridge Team"
        );

        return savedUser;
    }

    public Map<String, Object> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("role", user.getRole());
        response.put("name", user.getName());

        return response;
    }
}