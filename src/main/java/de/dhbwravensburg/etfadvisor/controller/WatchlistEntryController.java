package de.dhbwravensburg.etfadvisor.controller;

import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryRequest;
import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryResponse;
import de.dhbwravensburg.etfadvisor.mapper.WatchlistEntryMapper;
import de.dhbwravensburg.etfadvisor.service.EtfService;
import de.dhbwravensburg.etfadvisor.service.WatchlistEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/watchlist")
public class WatchlistEntryController {

    private final WatchlistEntryService watchlistEntryService;

    public WatchlistEntryController(WatchlistEntryService watchlistEntryService){this.watchlistEntryService=watchlistEntryService;}

    @GetMapping
    public List<WatchlistEntryResponse> getAll(){
        return this.watchlistEntryService.findAll().stream()
                .map(WatchlistEntryMapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<WatchlistEntryResponse> save( @Valid @RequestBody WatchlistEntryRequest watchlistEntryRequest){

        var entry = watchlistEntryService.create(watchlistEntryRequest.etfId(),watchlistEntryRequest);
        var response= WatchlistEntryMapper.toResponse(entry);
        return ResponseEntity.created(URI.create("/api/watchlist/"+response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        watchlistEntryService.delete(id);
    }
}

