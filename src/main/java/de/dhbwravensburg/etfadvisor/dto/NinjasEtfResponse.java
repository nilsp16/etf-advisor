package de.dhbwravensburg.etfadvisor.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NinjasEtfResponse(
        @JsonProperty("etf_ticker") String etfTicker,
        @JsonProperty("etf_name") String etfName,
        String isin,
        String country
) {
}
