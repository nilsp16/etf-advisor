package de.dhbwravensburg.etfadvisor.dto;

public record AuthRequest(
        String username,
        String password
) {
}
