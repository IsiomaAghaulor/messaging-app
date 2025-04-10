package com.example.messagingservice.service;

import com.example.messagingservice.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessagingService {
    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);
    
    private final WhatsAppService whatsAppService;
    private final EmailService emailService;
    private final TwilioAccountService twilioAccountService;

    public Map<String, String> setupTwilioAccount(TwilioAccountRequest request) {
        return twilioAccountService.createTwilioAccount(request);
    }

    public MessageResponseDetails sendMessage(MessageRequest request) {
        log.info("\n\n******* INITIATING MESSAGE SENDING PROCESS FOR RECIPIENT: {} *******", request.getPhoneNumber());
        
        try {
            // Check if WhatsApp is available for the number
            log.info("\n******* CHECKING WHATSAPP AVAILABILITY FOR NUMBER: {} *******", request.getPhoneNumber());
            boolean isWhatsAppAvailable = whatsAppService.isWhatsAppRegistered(request.getPhoneNumber());
            
            // If WhatsApp is not available, send via email
            if (!isWhatsAppAvailable) {
                log.info("\n******* WHATSAPP NOT AVAILABLE, SENDING VIA EMAIL TO: {} *******", request.getEmail());
                emailService.sendEmail(
                    request.getEmail(),
                    request.getSubject(),
                    request.getMessage()
                );
                
                MessageResponseDetails response = MessageResponseDetails.builder()
                    .recipient(request.getPhoneNumber())
                    .channel("email")
                    .usedExistingAccount(false)
                    .build();
                
                log.info("\n******* EMAIL SENT SUCCESSFULLY TO: {} *******", request.getEmail());
                return response;
            }
            
            // For WhatsApp messages, handle Twilio account setup
            log.info("\n******* HANDLING TWILIO ACCOUNT SETUP FOR WHATSAPP MESSAGE *******");
            String accountSid, authToken, twilioNumber;
            boolean isExistingAccount = request.hasExistingTwilioAccount();
            
            if (isExistingAccount) {
                log.info("\n******* VALIDATING EXISTING TWILIO ACCOUNT FOR SID: {} *******", request.getTwilioAccountSid());
                if (!twilioAccountService.validateTwilioAccount(
                        request.getTwilioAccountSid(),
                        request.getTwilioAuthToken())) {
                    log.error("\n******* INVALID TWILIO CREDENTIALS FOR SID: {} *******", request.getTwilioAccountSid());
                    throw new RuntimeException("Invalid Twilio credentials");
                }
                accountSid = request.getTwilioAccountSid();
                authToken = request.getTwilioAuthToken();
                twilioNumber = request.getTwilioPhoneNumber();
            } else {
                log.info("\n******* CREATING NEW TWILIO ACCOUNT FOR USER: {} *******", 
                    request.getFullName() != null ? request.getFullName() : request.getPhoneNumber());
                
                // Create new Twilio account
                TwilioAccountRequest twilioRequest = TwilioAccountRequest.builder()
                    .fullName(request.getFullName() != null ? request.getFullName() : "User " + request.getPhoneNumber())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .companyName(request.getCompanyName())
                    .build();
                
                Map<String, String> accountDetails = twilioAccountService.createTwilioAccount(twilioRequest);
                accountSid = accountDetails.get("accountSid");
                authToken = accountDetails.get("authToken");
                twilioNumber = accountDetails.get("twilioPhoneNumber");
                
                log.info("\n******* TWILIO ACCOUNT CREATED SUCCESSFULLY WITH SID: {} *******", accountSid);
            }
            
            // Send WhatsApp message
            log.info("\n******* SENDING WHATSAPP MESSAGE TO: {} *******", request.getPhoneNumber());
            whatsAppService.sendWhatsAppMessage(
                accountSid,
                authToken,
                twilioNumber,
                request.getPhoneNumber(),
                request.getMessage()
            );
            
            // Build response
            MessageResponseDetails response = MessageResponseDetails.builder()
                .recipient(request.getPhoneNumber())
                .channel("whatsapp")
                .usedExistingAccount(isExistingAccount)
                .twilioCredentials(isExistingAccount ? null : TwilioCredentials.builder()
                    .accountSid(accountSid)
                    .authToken(authToken)
                    .phoneNumber(twilioNumber)
                    .build())
                .build();
            
            log.info("\n******* MESSAGE SENT SUCCESSFULLY VIA WHATSAPP TO: {} *******", request.getPhoneNumber());
            return response;
            
        } catch (Exception e) {
            log.error("\n******* EXCEPTION CAUGHT SENDING MESSAGE. REASON: {} *******", e.getMessage());
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }
}
