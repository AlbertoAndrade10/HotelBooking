package com.poroflio.hotel_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequestDTO {
    @NotBlank(message = "Hotel name must not be blank")
    private String name;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    private String phoneNumber;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
