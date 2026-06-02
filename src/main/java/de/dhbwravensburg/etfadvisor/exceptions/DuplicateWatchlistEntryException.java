package de.dhbwravensburg.etfadvisor.exceptions;

public class DuplicateWatchlistEntryException extends RuntimeException {
    public DuplicateWatchlistEntryException() {
        super("This Entry already exists in your Watchlist");
    }
}
