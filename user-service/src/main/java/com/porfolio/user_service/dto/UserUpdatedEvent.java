package com.porfolio.user_service.dto;

import lombok.Data;

@Data
public class UserUpdatedEvent {
    private String id;
    private String email;
    private String username;
}
