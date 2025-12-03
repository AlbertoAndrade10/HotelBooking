package com.porfolio.user_service.service;

import java.util.List;

import com.porfolio.user_service.dto.UpdateUserProfileDto;
import com.porfolio.user_service.dto.UserProfileResponseDto;
import com.porfolio.user_service.dto.UserRegisteredEvent;
import com.porfolio.user_service.dto.UserUpdatedEvent;

public interface IUserProfileSerivce {

    // --- REST API METHODS ---
    List<UserProfileResponseDto> getAllProfiles();

    UserProfileResponseDto getProfile(String id);

    UserProfileResponseDto updateProfile(String id, UpdateUserProfileDto dto);

    // --- KAFKA EVENT METHODS ---
    void createProfileFromAuthEvent(UserRegisteredEvent event);

    void updatedProfileFromAuthEvent(UserUpdatedEvent event);
}
