package com.example.messagingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String responseCode;
    private String responseMessage;
    private Object responseDetails;
    
    public static ApiResponse success(String message, Object details) {
        return ApiResponse.builder()
                .responseCode("00")
                .responseMessage(message)
                .responseDetails(details)
                .build();
    }
    
    public static ApiResponse error(String message, Object details) {
        return ApiResponse.builder()
                .responseCode("99")
                .responseMessage(message)
                .responseDetails(details)
                .build();
    }
}
