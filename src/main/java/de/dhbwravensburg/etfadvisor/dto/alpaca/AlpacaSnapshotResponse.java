package de.dhbwravensburg.etfadvisor.dto.alpaca;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AlpacaSnapshotResponse(
        @JsonProperty("latestTrade") AlpacaLatestTrade latestTrade,
        @JsonProperty("dailyBar") AlpacaDailyBar dailyBar
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AlpacaLatestTrade(double p){}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AlpacaDailyBar(
            double o,
            double h,
            double l,
            double c,
            long v
    ){}
}
