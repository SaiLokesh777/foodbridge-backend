package com.foodbridge.foodbridgebackend.auth;

import com.foodbridge.foodbridgebackend.auth.dto.RegisterDonorRequest;
import com.foodbridge.foodbridgebackend.auth.dto.RegisterNgoRequest;
import com.foodbridge.foodbridgebackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.foodbridge.foodbridgebackend.auth.dto.LoginRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register/donor")
    public ResponseEntity<?> registerDonor(@RequestBody RegisterDonorRequest request) {
        User savedUser = authService.registerDonor(request);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/register/ngo")
    public ResponseEntity<?> registerNgo(@RequestBody RegisterNgoRequest request) {
        User savedUser = authService.registerNgo(request);
        return ResponseEntity.ok(savedUser);
    }
}