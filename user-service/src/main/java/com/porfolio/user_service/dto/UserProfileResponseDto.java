package com.porfolio.user_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponseDto {
    private String id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
}
