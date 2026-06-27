package de.dhbwravensburg.etfadvisor.controller;

import de.dhbwravensburg.etfadvisor.dto.WatchlistAddByTickerRequest;
import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryRequest;
import de.dhbwravensburg.etfadvisor.dto.WatchlistEntryResponse;
import de.dhbwravensburg.etfadvisor.mapper.WatchlistEntryMapper;
import de.dhbwravensburg.etfadvisor.service.EtfService;
import de.dhbwravensburg.etfadvisor.service.WatchlistEntryService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get current user's watchlist entries")
    @GetMapping
    public List<WatchlistEntryResponse> getAll(){
        return this.watchlistEntryService.findAll().stream()
                .map(WatchlistEntryMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Add an ETF to the watchlist")
    @PostMapping
    public ResponseEntity<WatchlistEntryResponse> save( @Valid @RequestBody WatchlistEntryRequest watchlistEntryRequest){

        var entry = watchlistEntryService.create(watchlistEntryRequest.etfId(),watchlistEntryRequest);
        var response= WatchlistEntryMapper.toResponse(entry);
        return ResponseEntity.created(URI.create("/api/watchlist/"+response.id())).body(response);
    }

    @Operation(summary = "Add an ETF to the watchlist by ticker (auto-creates ETF if needed)")
    @PostMapping("/add-by-ticker")
   public ResponseEntity<WatchlistEntryResponse> addByTicker(@RequestBody WatchlistAddByTickerRequest request){
        var entry = watchlistEntryService.addByTicker(request.ticker(),request.userNote());
        var response = WatchlistEntryMapper.toResponse(entry);
        return ResponseEntity.created(URI.create("/api/watchlist/"+response.id())).body(response);
   }

    @Operation(summary = "Remove an entry from the watchlist")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        watchlistEntryService.delete(id);
    }
}

