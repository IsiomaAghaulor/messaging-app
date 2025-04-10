package com.example.messagingservice.controller;

import com.example.messagingservice.model.ApiResponse;
import com.example.messagingservice.model.TwilioAccountRequest;
import com.example.messagingservice.service.MessagingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/twilio")
@RequiredArgsConstructor
public class TwilioAccountController {
    private final MessagingService messagingService;

    @PostMapping("/setup")
    public ResponseEntity<ApiResponse> setupTwilioAccount(@Valid @RequestBody TwilioAccountRequest request) {
        try {
            Map<String, String> accountDetails = messagingService.setupTwilioAccount(request);
            return ResponseEntity.ok(ApiResponse.success(
                "Twilio account setup successful",
                accountDetails
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                "Failed to setup Twilio account",
                e.getMessage()
            ));
        }
    }
}
