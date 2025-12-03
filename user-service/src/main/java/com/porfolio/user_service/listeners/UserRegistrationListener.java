package com.porfolio.user_service.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfolio.user_service.dto.UserRegisteredEvent;
import com.porfolio.user_service.service.IUserProfileSerivce;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegistrationListener {

    private final IUserProfileSerivce userService;
    private final ObjectMapper objectMapper;

    public UserRegistrationListener(IUserProfileSerivce userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "user-registered", groupId = "user-service-group")
    public void handleUserRegistered(String message) {
        try {
            UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);
            userService.createProfileFromAuthEvent(event);
            log.info("Profile created for {}", event.getId());

        } catch (Exception e) {
            log.error("Failed to process user-registered event", e);
        }
    }
}
