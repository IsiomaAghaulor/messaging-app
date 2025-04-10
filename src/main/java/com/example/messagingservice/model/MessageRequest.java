package com.example.messagingservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Message content is required")
    private String message;
    
    private String subject;  // Optional for email
    
    // Optional Twilio account details - if not provided, new account will be created
    private String twilioAccountSid;
    private String twilioAuthToken;
    private String twilioPhoneNumber;
    
    // User details for new Twilio account creation if needed
    private String fullName;
    private String companyName;
    
    public boolean hasExistingTwilioAccount() {
        return twilioAccountSid != null && twilioAuthToken != null && twilioPhoneNumber != null;
    }
}
