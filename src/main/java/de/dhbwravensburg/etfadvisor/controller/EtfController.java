package de.dhbwravensburg.etfadvisor.controller;


import de.dhbwravensburg.etfadvisor.dto.EtfRequest;
import de.dhbwravensburg.etfadvisor.dto.EtfResponse;
import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.mapper.EtfMapper;
import de.dhbwravensburg.etfadvisor.service.EtfService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/etfs")
public class EtfController {


    private final EtfService etfService;

    public EtfController(EtfService etfService){
        this.etfService = etfService;
    }

    @Operation(summary = "Get all ETFs")
    @GetMapping
    public List<EtfResponse> getAll(){
        return this.etfService.findAll().stream()
                .map(EtfMapper :: toResponse )
                .toList();
    }

    @Operation(summary = "Get ETF by ID")
    @GetMapping("/{id}")
    public EtfResponse getById(@PathVariable long id){
    return EtfMapper.toResponse(etfService.findById(id));
    }

    @Operation(summary = "Get ETF by ISIN")
    @GetMapping("/isin/{isin}")
    public  EtfResponse getByIsin(@PathVariable String isin){
        return EtfMapper.toResponse(etfService.findByIsin(isin));    }


    @Operation(summary = "Get ETFs by dividend policy")
    @GetMapping("/dividend/{dividendPolicy}")
    public List<EtfResponse> getByDividendPolicy(@PathVariable String dividendPolicy){
        return this.etfService.findByDividendPolicy(dividendPolicy)
                .stream()
                .map(EtfMapper::toResponse)
                .toList();

    }

    @Operation(summary = "Get ETF by ticker symbol")
    @GetMapping("/ticker/{ticker}")
    public  EtfResponse getByTicker(@PathVariable String ticker){
        return EtfMapper.toResponse(etfService.findByTicker(ticker));    }

    @Operation(summary = "Create a new ETF (ADMIN only)")
    @PostMapping
    public ResponseEntity<EtfResponse> save(@Valid @RequestBody EtfRequest etfRequest){
        var created = etfService.create(EtfMapper.toEntity(null, etfRequest));
        var response = EtfMapper.toResponse(created);
        return ResponseEntity
                .created(URI.create("api/etfs/" + created.getId()))
                .body(response);
    }

    @Operation(summary = "Update ETF metadata (ADMIN only)")
    @PutMapping("/{id}")
    public EtfResponse update(@PathVariable Long id,@Valid @RequestBody EtfRequest etfRequest){
        var updated = etfService.update(id, EtfMapper.toEntity(id,etfRequest));
        return EtfMapper.toResponse(updated);

    }

    @Operation(summary = "Delete an ETF (ADMIN only)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        etfService.delete(id);
    }


}
