package com.porfolio.user_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.porfolio.user_service.dto.UpdateUserProfileDto;
import com.porfolio.user_service.dto.UserProfileResponseDto;
import com.porfolio.user_service.dto.UserRegisteredEvent;
import com.porfolio.user_service.dto.UserUpdatedEvent;
import com.porfolio.user_service.entity.UserProfile;
import com.porfolio.user_service.exceptions.ProfileNotFoundException;
import com.porfolio.user_service.exceptions.UserNotFoundException;
import com.porfolio.user_service.repository.UserProfileRepository;

@Service
public class UserProfileServiceImpl implements IUserProfileSerivce {

    private final UserProfileRepository repository;

    public UserProfileServiceImpl(UserProfileRepository repository) {
        this.repository = repository;
    }

    // -------------------------------------------------------
    // REST METHODS
    // -------------------------------------------------------

    @Override
    public List<UserProfileResponseDto> getAllProfiles() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserProfileResponseDto getProfile(String id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserProfileResponseDto updateProfile(String id, UpdateUserProfileDto dto) {

        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(id));

        if (dto.getUsername() != null)
            profile.setUsername(dto.getUsername());

        if (dto.getFirstName() != null)
            profile.setFirstName(dto.getFirstName());

        if (dto.getLastName() != null)
            profile.setLastName(dto.getLastName());

        if (dto.getPhone() != null)
            profile.setPhone(dto.getPhone());

        return toDto(repository.save(profile));
    }

    // -------------------------------------------------------
    // KAFKA EVENT METHODS
    // -------------------------------------------------------

    @Override
    public void createProfileFromAuthEvent(UserRegisteredEvent event) {

        // Si ya existe, no hacemos nada
        if (repository.existsById(event.getId())) {
            System.out.println("User profile already exists, skipping...");
            return;
        }

        UserProfile profile = new UserProfile();
        profile.setId(event.getId());
        profile.setEmail(event.getEmail());
        profile.setUsername(event.getUsername());

        repository.save(profile);
    }

    @Override
    public void updatedProfileFromAuthEvent(UserUpdatedEvent event) {

        UserProfile profile = repository.findById(event.getId())
                .orElseThrow(() -> new UserNotFoundException(event.getId()));

        if (event.getEmail() != null)
            profile.setEmail(event.getEmail());

        if (event.getUsername() != null)
            profile.setUsername(event.getUsername());

        repository.save(profile);
    }

    // -------------------------------------------------------
    // PRIVATE HELPERS
    // -------------------------------------------------------

    private UserProfileResponseDto toDto(UserProfile profile) {
        return UserProfileResponseDto.builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phone(profile.getPhone())
                .build();
    }
}
