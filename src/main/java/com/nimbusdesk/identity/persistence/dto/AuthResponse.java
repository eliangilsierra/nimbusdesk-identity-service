package com.nimbusdesk.identity.persistence.dto;

public record AuthResponse(
        String token,
        String email,
        String role
) {
}
