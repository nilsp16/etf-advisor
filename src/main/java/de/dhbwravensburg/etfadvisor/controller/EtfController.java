package de.dhbwravensburg.etfadvisor.controller;


import de.dhbwravensburg.etfadvisor.dto.EtfRequest;
import de.dhbwravensburg.etfadvisor.dto.EtfResponse;
import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.mapper.EtfMapper;
import de.dhbwravensburg.etfadvisor.service.EtfService;
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

    @GetMapping
    public List<EtfResponse> getAll(){
        return this.etfService.findAll().stream()
                .map(EtfMapper :: toResponse )
                .toList();
    }

    @GetMapping("/{id}")
    public EtfResponse getById(@PathVariable long id){
    return EtfMapper.toResponse(etfService.findById(id));
    }

    @GetMapping("/isin/{isin}")
    public  EtfResponse getByIsin(@PathVariable String isin){
        return EtfMapper.toResponse(etfService.findByIsin(isin));    }



    @GetMapping("/dividend/{dividendPolicy}")
    public List<EtfResponse> getByDividendPolicy(@PathVariable String dividendPolicy){
        return this.etfService.findByDividendPolicy(dividendPolicy)
                .stream()
                .map(EtfMapper::toResponse)
                .toList();

    }


    @GetMapping("/ticker/{ticker}")
    public  EtfResponse getByTicker(@PathVariable String ticker){
        return EtfMapper.toResponse(etfService.findByTicker(ticker));    }


    @PostMapping
    public ResponseEntity<EtfResponse> save(@Valid @RequestBody EtfRequest etfRequest){
        var created = etfService.create(EtfMapper.toEntity(null, etfRequest));
        var response = EtfMapper.toResponse(created);
        return ResponseEntity
                .created(URI.create("api/etfs/" + created.getId()))
                .body(response);
    }
    @PutMapping("/{id}")
    public EtfResponse update(@PathVariable Long id,@Valid @RequestBody EtfRequest etfRequest){
        var updated = etfService.update(id, EtfMapper.toEntity(id,etfRequest));
        return EtfMapper.toResponse(updated);

    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        etfService.delete(id);
    }


}
