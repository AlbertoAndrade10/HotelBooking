package com.porfolio.user_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porfolio.user_service.dto.UpdateUserProfileDto;
import com.porfolio.user_service.dto.UserProfileResponseDto;
import com.porfolio.user_service.service.IUserProfileSerivce;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserProfileSerivce service;

    public UserController(IUserProfileSerivce service) {
        this.service = service;
    }

    /**
     * Retrieves all user profiles stored in the system.
     *
     * @return A list of UserProfileResponseDto objects.
     */
    @GetMapping
    public List<UserProfileResponseDto> getAllProfiles() {
        return service.getAllProfiles();
    }

    /**
     * Retrieves a specific user profile by its ID.
     *
     * @param id The user ID provided as a path variable.
     * @return 200 OK with the user profile if found,
     *         otherwise 404 Not Found.
     */
    @GetMapping("/{id}")
    public UserProfileResponseDto getProfile(@PathVariable String id) {
        return service.getProfile(id);
    }

    /**
     * Updates a user profile with the provided data.
     *
     * @param id  The ID of the user profile to update.
     * @param dto The new profile data sent in the request body.
     * @return 200 OK with the updated UserProfileResponseDto.
     */
    @PutMapping("/{id}")
    public UserProfileResponseDto updateProfile(
            @PathVariable String id,
            @RequestBody @Valid UpdateUserProfileDto dto) {

        return service.updateProfile(id, dto);
    }
}
