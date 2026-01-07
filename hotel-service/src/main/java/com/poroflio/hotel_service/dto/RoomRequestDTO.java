package com.poroflio.hotel_service.dto;

import java.math.BigDecimal;
import com.poroflio.hotel_service.entity.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDTO {

    @NotBlank(message = "Room number must not be blank")
    private String number;

    @NotNull(message = "Hotel ID must not be null")
    private String hotelId;

    @NotNull(message = "Room type must not be null")
    private RoomType roomType;

    @NotNull(message = "Availability status must not be null")
    private Boolean isAvailable;

    private BigDecimal pricePerNight;
}
