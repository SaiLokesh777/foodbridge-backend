package com.foodbridge.foodbridgebackend.notification;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class OtpStore {

    private final Map<String, OtpEntry> store = new HashMap<>();

    public String generateAndStore(String phone) {
        String otp = String.format("%04d", new Random().nextInt(10000));
        store.put(phone, new OtpEntry(otp, LocalDateTime.now().plusMinutes(10)));
        return otp;
    }

    public boolean verify(String phone, String otp) {
        OtpEntry entry = store.get(phone);
        if (entry == null) return false;
        if (LocalDateTime.now().isAfter(entry.expiry)) {
            store.remove(phone);
            return false;
        }
        if (entry.otp.equals(otp)) {
            store.remove(phone);
            return true;
        }
        return false;
    }

    private static class OtpEntry {
        String otp;
        LocalDateTime expiry;
        OtpEntry(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }
}