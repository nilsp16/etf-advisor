package de.dhbwravensburg.etfadvisor.controller;


import de.dhbwravensburg.etfadvisor.client.AlpacaMarketDataClient;
import de.dhbwravensburg.etfadvisor.dto.alpaca.MarketDataResponse;
import de.dhbwravensburg.etfadvisor.service.EtfService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/market-data")
public class MarketDataController {

    private final EtfService service;
    private final AlpacaMarketDataClient client;


    public MarketDataController(EtfService service,AlpacaMarketDataClient client){
        this.service = service;
        this.client = client;
    }

    @Operation(summary = "Get live market data from Alpaca for an ETF")
    @GetMapping("/{etfId}")
    public MarketDataResponse getEtfTickerById(@PathVariable Long etfId){
        var etfTicker = service.findById(etfId).getTicker();
        var responseAPI = client.fetchSnapshot(etfTicker);
        return new MarketDataResponse(
                etfId,
                etfTicker,
                responseAPI.latestTrade().p(),
                responseAPI.dailyBar().o(),
                responseAPI.dailyBar().h(),
                responseAPI.dailyBar().l(),
                responseAPI.dailyBar().c(),
                responseAPI.dailyBar().v()
                );



    }
}
