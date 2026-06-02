package de.dhbwravensburg.etfadvisor.dto;
import java.time.LocalDateTime;

public record WatchlistEntryResponse(
        Long id,
        Long etfId,
        String etfName,
        LocalDateTime addedAt,
        String userNote
) {
}
