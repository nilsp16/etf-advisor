package de.dhbwravensburg.etfadvisor.dto;

public record WatchlistAddByTickerRequest(
        String ticker,
        String userNote
) {
}
