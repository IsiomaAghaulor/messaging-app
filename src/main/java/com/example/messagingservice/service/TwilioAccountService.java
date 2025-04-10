package com.example.messagingservice.service;

import com.example.messagingservice.model.TwilioAccountRequest;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.NewKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@Service
@lombok.extern.slf4j.Slf4j
@RequiredArgsConstructor
public class TwilioAccountService {
    @Value("${twilio.master.sid:}")
    private String MASTER_ACCOUNT_SID;

    @Value("${twilio.master.token:}")
    private String MASTER_AUTH_TOKEN;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> createTwilioAccount(TwilioAccountRequest request) {
        // Initialize with master account
        log.info ("\nSID: {}, TOKEN: {}", MASTER_ACCOUNT_SID, MASTER_AUTH_TOKEN);
        Twilio.init(MASTER_ACCOUNT_SID, MASTER_AUTH_TOKEN);

        try {
            // Create Twilio sub-account
            Map<String, String> accountDetails = createSubAccount(request);
            
            // Create API Key for the sub-account
            NewKey newKey = NewKey.creator().create();
            
            // Store the account details
            accountDetails.put("accountSid", accountDetails.get("sid"));
            accountDetails.put("authToken", newKey.getSecret());
            
            // Purchase phone number for WhatsApp (in production, you'd want to implement number selection)
            String phoneNumber = purchasePhoneNumber(accountDetails.get("sid"), newKey.getSecret());
            accountDetails.put("twilioPhoneNumber", phoneNumber);
            
            return accountDetails;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Twilio account: " + e.getMessage());
        }
    }

    private Map<String, String> createSubAccount(TwilioAccountRequest request) {
        String url = "https://api.twilio.com/2010-04-01/Accounts.json";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(MASTER_ACCOUNT_SID, MASTER_AUTH_TOKEN);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("FriendlyName", request.getFullName());
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            Map.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
            return (Map<String, String>) response.getBody();
        } else {
            throw new RuntimeException("Failed to create Twilio sub-account");
        }
    }

    private String purchasePhoneNumber(String accountSid, String authToken) {
        // In a real implementation, you would:
        // 1. Search for available numbers in the user's country
        // 2. Let the user select a number
        // 3. Purchase the selected number
        // For demo purposes, we'll return a placeholder
        return "+1234567890";
    }

    public boolean validateTwilioAccount(String accountSid, String authToken) {
        try {
            Twilio.init(accountSid, authToken);
            // Make a test API call to verify credentials
            com.twilio.rest.api.v2010.account.Message.reader().limit(1).read();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
