package de.dhbwravensburg.etfadvisor.client;

import de.dhbwravensburg.etfadvisor.dto.NinjasEtfResponse;
import de.dhbwravensburg.etfadvisor.dto.alpaca.AlpacaSnapshotResponse;
import de.dhbwravensburg.etfadvisor.exceptions.ExternalApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class NinjasEtfClient {

    private final RestClient ninjaRestClient;

    public NinjasEtfClient(@Qualifier("ninjasRestClient") RestClient ninjaRestClient) {
        this.ninjaRestClient = ninjaRestClient;

    }

    public NinjasEtfResponse getMeta(String ticker) {
        try {
            NinjasEtfResponse response = ninjaRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/etf?ticker=" + ticker)
                            .build()
                    )
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        throw new ExternalApiException("Ninjas API client error:" + res.getStatusCode());

                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new ExternalApiException("Ninjas API server error:" + res.getStatusCode());
                    })
                    .body(NinjasEtfResponse.class);

            if (response == null) {
                throw new ExternalApiException("No data from Ninjas for symbol: " + ticker);
            }


            return response;
        } catch (RestClientException ex) {
            throw new ExternalApiException("Failed to call Ninjas:" + ex.getMessage(), ex);
        }


    }
}

