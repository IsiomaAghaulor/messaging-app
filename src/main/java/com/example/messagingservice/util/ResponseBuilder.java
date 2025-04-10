package com.example.messagingservice.util;

import com.example.messagingservice.model.ApiResponse;
import com.example.messagingservice.model.MessageResponseDetails;
import com.example.messagingservice.model.TwilioCredentials;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    
    public static ResponseEntity<ApiResponse> successResponse(MessageResponseDetails details) {
        return ResponseEntity.ok(ApiResponse.success(
            "Message sent successfully",
            details
        ));
    }
    
    public static ResponseEntity<ApiResponse> errorResponse(String message, Object details) {
        return ResponseEntity.badRequest().body(ApiResponse.error(
            message,
            details
        ));
    }
    
    public static ResponseEntity<ApiResponse> validationErrorResponse(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errorResponse("Validation failed", errors);
    }
    
    public static MessageResponseDetails.MessageResponseDetailsBuilder createMessageResponseDetails(
            String recipient,
            String channel,
            boolean usedExistingAccount) {
        return MessageResponseDetails.builder()
            .recipient(recipient)
            .channel(channel)
            .usedExistingAccount(usedExistingAccount);
    }
    
    public static TwilioCredentials createTwilioCredentials(
            String accountSid,
            String authToken,
            String phoneNumber) {
        return TwilioCredentials.builder()
            .accountSid(accountSid)
            .authToken(authToken)
            .phoneNumber(phoneNumber)
            .build();
    }
}
