package de.dhbwravensburg.etfadvisor.controller;


import de.dhbwravensburg.etfadvisor.entity.Etf;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/etfs")
public class EtfController {
    private final List<Etf> etfs = Etf.generateTestData();

    @GetMapping
    public List<Etf> getAll(){
        return this.etfs;
    }

    @GetMapping("/{id}")
    public Etf getById(@PathVariable long id){
        return this.etfs.stream()
                .filter(etf -> etf.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/isin/{isin}")
    public  Etf getByIsin(@PathVariable String isin){
        return this.etfs.stream()
                .filter(etf -> etf.getIsin().equals(isin))
                .findFirst()
                .orElse(null);
    }
    /*@GetMapping("/accumulating")
    public List<Etf> getAccumulating(){
        return this.etfs.stream()
                .filter(etf -> etf.getDividendPolicy().equalsIgnoreCase("Accumulating"))
                .toList();

    }*/

    @GetMapping("/dividend/{dividendPolicy}")
    public List<Etf> getByDividendPolicy(@PathVariable String dividendPolicy){
        return this.etfs.stream()
                .filter(etf -> etf.getDividendPolicy().equalsIgnoreCase(dividendPolicy))
                .toList();

    }
}
