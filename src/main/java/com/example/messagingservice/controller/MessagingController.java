package com.example.messagingservice.controller;

import com.example.messagingservice.model.ApiResponse;
import com.example.messagingservice.model.MessageRequest;
import com.example.messagingservice.model.MessageResponseDetails;
import com.example.messagingservice.service.MessagingService;
import com.example.messagingservice.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Slf4j
public class MessagingController {
    //private static final Logger log = LoggerFactory.getLogger(MessagingController.class);
    
    private final MessagingService messagingService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        log.info("\n\n******* RECEIVED MESSAGE SEND REQUEST FOR RECIPIENT: {} *******", request.getPhoneNumber());
        
        try {
            MessageResponseDetails responseDetails = messagingService.sendMessage(request);
            log.info("\n******* MESSAGE SEND REQUEST COMPLETED SUCCESSFULLY FOR RECIPIENT: {} *******", request.getPhoneNumber());
            return ResponseBuilder.successResponse(responseDetails);
        } catch (Exception e) {
            log.info("\n******* MESSAGE SEND REQUEST FAILED FOR RECIPIENT: {}. REASON: {} *******", request.getPhoneNumber(), e.getMessage());
            return ResponseBuilder.errorResponse("Failed to send message", e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("\n******* VALIDATION ERROR IN MESSAGE SEND REQUEST *******");
        return ResponseBuilder.validationErrorResponse(ex);
    }
}
