package de.dhbwravensburg.etfadvisor.controller;


import de.dhbwravensburg.etfadvisor.dto.EtfRequest;
import de.dhbwravensburg.etfadvisor.dto.EtfResponse;
import de.dhbwravensburg.etfadvisor.entity.Etf;
import de.dhbwravensburg.etfadvisor.mapper.EtfMapper;
import de.dhbwravensburg.etfadvisor.service.EtfService;
import jakarta.validation.Valid;
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
    public ResponseEntity<EtfResponse> getById(@PathVariable long id){
       return this.etfService.findById(id)
               .map(EtfMapper :: toResponse )
               .map(ResponseEntity :: ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isin/{isin}")
    public  ResponseEntity<EtfResponse> getByIsin(@PathVariable String isin){
        return this.etfService.findByIsin(isin)
                .map(EtfMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/dividend/{dividendPolicy}")
    public List<EtfResponse> getByDividendPolicy(@PathVariable String dividendPolicy){
        return this.etfService.findByDividendPolicy(dividendPolicy)
                .stream()
                .map(EtfMapper::toResponse)
                .toList();

    }


    @GetMapping("/ticker/{ticker}")
    public  ResponseEntity<EtfResponse> getByTicker(@PathVariable String ticker){
        return this.etfService.findByTicker(ticker)
                .map(entity ->EtfMapper.toResponse(entity))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<EtfResponse> save(@Valid @RequestBody EtfRequest etfRequest){
        var created = etfService.create(EtfMapper.toEntity(null, etfRequest));
        var response = EtfMapper.toResponse(created);
        return ResponseEntity
                .created(URI.create("api/etfs/" + created.getId()))
                .body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EtfResponse> update(@PathVariable Long id,@Valid @RequestBody EtfRequest etfRequest){
        return etfService.update(id,EtfMapper.toEntity(id, etfRequest))
                .map(EtfMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<EtfResponse> delete(@PathVariable Long id){
        if (etfService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }


}
