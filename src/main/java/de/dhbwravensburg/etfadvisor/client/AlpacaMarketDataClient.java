package de.dhbwravensburg.etfadvisor.client;

import de.dhbwravensburg.etfadvisor.dto.alpaca.AlpacaSnapshotResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;

@Service
public class AlpacaMarketDataClient {
    private final RestClient alpacaRestClient;

    public AlpacaMarketDataClient(RestClient alpacaRestClient){
        this.alpacaRestClient = alpacaRestClient;

    }

    public AlpacaSnapshotResponse fetchSnapshot(String symbol){
        try{
            AlpacaSnapshotResponse response = alpacaRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/stocks/" + symbol + "/snapshot")
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

            if(response == null || response.dailyBar()== null){
                return Collections.emptyList();
            }

            return response.
        }catch (RestClientException ex){
            throw  new ExternalApiException("Failed to call Alpaca:" + ex.getMessage(), ex);
        }

    }


}
