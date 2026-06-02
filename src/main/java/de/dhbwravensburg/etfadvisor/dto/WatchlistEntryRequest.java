package de.dhbwravensburg.etfadvisor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WatchlistEntryRequest(
        @NotNull(message = "etfId must not be null")
        @Positive(message = "etfId must be positive")
        Long etfId,
        String userNote
) {
}
