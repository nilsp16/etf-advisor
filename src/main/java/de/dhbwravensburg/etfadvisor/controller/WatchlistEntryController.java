package de.dhbwravensburg.etfadvisor.controller;

import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryRequest;
import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryResponse;
import de.dhbwravensburg.etfadvisor.mapper.WatchlistEntryMapper;
import de.dhbwravensburg.etfadvisor.service.EtfService;
import de.dhbwravensburg.etfadvisor.service.WatchlistEntryService;
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
    public ResponseEntity<WatchlistEntryResponse> save(@RequestBody WatchlistEntryRequest watchlistEntryRequest){

        return  watchlistEntryService.create(watchlistEntryRequest.etfId(), watchlistEntryRequest)
                .map(WatchlistEntryMapper::toResponse)
                .map(response ->ResponseEntity
                        .created(URI.create("/api/watchlist/"+response.id()))
                        .body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WatchlistEntryResponse> delete(@PathVariable Long id){
        if (watchlistEntryService.delete(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}

