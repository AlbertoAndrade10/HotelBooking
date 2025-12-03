package com.porfolio.user_service.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfolio.user_service.dto.UserUpdatedEvent;
import com.porfolio.user_service.service.IUserProfileSerivce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserUpdatedListener {

    private static final Logger logger = LoggerFactory.getLogger(UserUpdatedListener.class);

    @Autowired
    private IUserProfileSerivce profileSerivce;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "user-updated", groupId = "user-service-group")
    public void handleUserUpdated(String message) {
        try {
            logger.info("Received user-updated event: {}", message);

            UserUpdatedEvent event = objectMapper.readValue(message, UserUpdatedEvent.class);

            profileSerivce.updatedProfileFromAuthEvent(event);
        } catch (Exception e) {
            logger.error("Error processing user-updated event: {} ", e.getMessage());
        }
    }
}
