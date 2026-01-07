package com.poroflio.hotel_service.dto;

import java.math.BigDecimal;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {
    private String id;
    private String number;
    private String roomType;
    private Boolean isAvailable;
    private BigDecimal pricePerNight;
}
