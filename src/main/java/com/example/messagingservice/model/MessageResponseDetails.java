package com.example.messagingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDetails {
    private String recipient;
    private String channel;
    private boolean usedExistingAccount;
    private TwilioCredentials twilioCredentials;  // Optional, only present when new account is created
}
