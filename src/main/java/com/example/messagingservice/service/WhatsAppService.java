package com.example.messagingservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service("whatsAppService")
public class WhatsAppService {
    
    public boolean isWhatsAppRegistered(String phoneNumber) {
        // In a real application, you would use Twilio's API to check if the number is registered
        // For demo purposes, we'll assume numbers starting with '+234' are registered
        return phoneNumber != null && phoneNumber.startsWith("+234");
    }

    public void sendWhatsAppMessage(String accountSid, String authToken, String fromNumber, String to, String messageContent) {
        if (accountSid == null || authToken == null || fromNumber == null || to == null || messageContent == null) {
            throw new IllegalArgumentException("All parameters must be non-null");
        }
        
        Twilio.init(accountSid, authToken);
        Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber("whatsapp:" + fromNumber),
                messageContent
        ).create();
    }
}
