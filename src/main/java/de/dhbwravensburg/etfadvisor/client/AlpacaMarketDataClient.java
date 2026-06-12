package de.dhbwravensburg.etfadvisor.client;

import de.dhbwravensburg.etfadvisor.dto.alpaca.AlpacaSnapshotResponse;
import de.dhbwravensburg.etfadvisor.exceptions.ExternalApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;

@Service
public class AlpacaMarketDataClient {
    private final RestClient alpacaRestClient;

    public AlpacaMarketDataClient(@Qualifier("alpacaRestClient") RestClient alpacaRestClient){
        this.alpacaRestClient = alpacaRestClient;

    }

    public AlpacaSnapshotResponse fetchSnapshot(String symbol){
        try{
            AlpacaSnapshotResponse response = alpacaRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/stocks/" + symbol + "/snapshot")
                            .build()
                    )
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,(req,res)->{
                        throw  new ExternalApiException("Alpaca API client error:" + res.getStatusCode());

                    })
                    .onStatus(HttpStatusCode::is5xxServerError,(req,res)->{
                        throw new ExternalApiException("Alpaca API server error:" + res.getStatusCode());
                    })
                    .body(AlpacaSnapshotResponse.class);

            if (response == null) {
                throw new ExternalApiException("No data from Alpaca for symbol: " + symbol);
            }


            return response;
        }catch (RestClientException ex){
            throw  new ExternalApiException("Failed to call Alpaca:" + ex.getMessage(), ex);
        }

    }


}
