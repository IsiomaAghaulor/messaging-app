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
public class TwilioAccountRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    private String companyName;  // Optional
    
    // For existing Twilio accounts
    private String accountSid;
    private String authToken;
    private String twilioPhoneNumber;
    
    public boolean hasExistingAccount() {
        return accountSid != null && authToken != null && twilioPhoneNumber != null;
    }
}
