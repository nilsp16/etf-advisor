package de.dhbwravensburg.etfadvisor.mapper;

import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryRequest;
import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryResponse;
import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.entity.WatchlistEntry;

import java.time.LocalDateTime;

public class WatchlistEntryMapper {

    public static WatchlistEntry toEntity(WatchlistEntryRequest request, Etf  etf) {
        return new WatchlistEntry(
                null,
                etf,
                LocalDateTime.now(),
                request.userNote()
        );
    }

    public static WatchlistEntryResponse toResponse(WatchlistEntry entry){
        return new WatchlistEntryResponse(
                entry.getId(),
                entry.getEtf().getId(),
                entry.getEtf().getName(),
                entry.getAddedAt(),
                entry.getUserNote()
        );
    }




}
