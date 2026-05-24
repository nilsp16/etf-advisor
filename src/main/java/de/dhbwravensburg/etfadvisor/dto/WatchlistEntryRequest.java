package de.dhbwravensburg.etfadvisor.dto;

public record WatchlistEntryRequest(

        Long etfId,
        String userNote
) {
}
