package com.replied.followapi.dto;

import java.util.UUID;

public class FollowResponseDTO {
    private UUID userId;
    private String username;
    private String birthdate;  // Can be hidden if not close friends

    public FollowResponseDTO(UUID userId, String username, String birthdate) {
        this.userId = userId;
        this.username = username;
        this.birthdate = birthdate;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
