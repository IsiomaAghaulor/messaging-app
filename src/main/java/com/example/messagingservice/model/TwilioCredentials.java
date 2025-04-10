package com.example.messagingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwilioCredentials {
    private String accountSid;
    private String authToken;
    private String phoneNumber;
}
