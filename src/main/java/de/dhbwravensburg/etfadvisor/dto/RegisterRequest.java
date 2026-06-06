package de.dhbwravensburg.etfadvisor.dto;

public record RegisterRequest(
        String username,
        String password,
        String role
) {
}
