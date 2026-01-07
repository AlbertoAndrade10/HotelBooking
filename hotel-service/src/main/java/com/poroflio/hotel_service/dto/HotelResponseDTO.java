package com.poroflio.hotel_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HotelResponseDTO {
    private String id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private String description;

    private List<RoomResponseDTO> rooms;
}
