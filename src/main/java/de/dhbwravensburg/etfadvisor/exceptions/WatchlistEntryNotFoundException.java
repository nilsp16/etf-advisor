package de.dhbwravensburg.etfadvisor.exceptions;

import de.dhbwravensburg.etfadvisor.entity.WatchlistEntry;

public class WatchlistEntryNotFoundException extends RuntimeException {

    public WatchlistEntryNotFoundException(Long etfId) {
        super("Etf with"+ etfId + "id, not found");
    }

}

