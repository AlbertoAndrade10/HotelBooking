package com.porfolio.user_service.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfileDto {

    @Size(min = 2, max = 20)
    private String username;

    @Size(min = 2, max = 40)
    private String firstName;

    @Size(min = 2, max = 40)
    private String lastName;

    @Pattern(regexp = "^[0-9+\\-]{7,15}$")
    private String phone;
}
