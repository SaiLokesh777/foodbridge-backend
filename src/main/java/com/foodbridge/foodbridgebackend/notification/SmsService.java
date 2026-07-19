package com.foodbridge.foodbridgebackend.notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.api.key}")
    private String apiKey;

    @Value("${twilio.api.secret}")
    private String apiSecret;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    public void sendSms(String toNumber, String message) {
        try {
            Twilio.init(apiKey, apiSecret, accountSid);
            Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(fromNumber),
                message
            ).create();
            System.out.println("SMS sent to: " + toNumber);
        } catch (Exception e) {
            System.out.println("SMS failed to " + toNumber + ": " + e.getMessage());
        }
    }

    public void sendOtp(String toNumber, String otp) {
        sendSms(toNumber,
            "Your Food Bridge verification code is: " + otp +
            "\nValid for 10 minutes. Do not share this with anyone."
        );
    }
}