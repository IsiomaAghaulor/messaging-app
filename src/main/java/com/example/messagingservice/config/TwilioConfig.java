package com.example.messagingservice.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class TwilioConfig {
    @Value("${twilio.account.sid:}")
    private String accountSid;

    @Value("${twilio.auth.token:}")
    private String authToken;

    @Value("${twilio.phone.number:}")
    private String phoneNumber;

    @Value("${twilio.master.sid:}")
    private String masterSid;

    @Value("${twilio.master.token:}")
    private String masterToken;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void initTwilio() {
        if (masterSid != null && !masterSid.isEmpty() && masterToken != null && !masterToken.isEmpty()) {
            Twilio.init(masterSid, masterToken);
        } else if (accountSid != null && !accountSid.isEmpty() && authToken != null && !authToken.isEmpty()) {
            Twilio.init(accountSid, authToken);
        }
    }
}
